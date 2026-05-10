package com.splitbill.expense.service.impl;

import com.splitbill.common.dto.expenses.request.CreateExpenseRequest;
import com.splitbill.common.dto.expenses.response.ExpenseResponse;
import com.splitbill.common.dto.expenses.response.SplitResponse;
import com.splitbill.expense.entity.ExpenseSplits;
import com.splitbill.expense.entity.Expenses;
import com.splitbill.expense.repository.ExpensesRepository;
import com.splitbill.expense.service.ExpenseService;
import com.splitbill.expense.service.SplitExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpensesRepository expensesRepository;
    private final SplitExpenseService splitExpenseService;

    @Override
    @Transactional
    public ExpenseResponse createExpense(Long groupId, CreateExpenseRequest request) {
        Expenses expense = Expenses.builder()
                .groupId(groupId)
                .title(request.getTitle())
                .description(request.getDescription())
                .amount(request.getAmount())
                .paidByParticipantId(request.getPaidByParticipantId())
                .paidByName(request.getPaidByName())
                .category(request.getCategory())
                .splitType(request.getSplitType())
                .expenseDate(request.getExpenseDate() != null ? request.getExpenseDate() : LocalDate.now())
                .splits(new ArrayList<>())
                .build();

        List<ExpenseSplits> splits = splitExpenseService.calculateSplits(
                request.getAmount(),
                request.getSplitType(),
                request.getSplits()
        );

        expense.setSplits(splits);
        splits.forEach(split -> split.setExpense(expense));

        Expenses savedExpense = expensesRepository.save(expense);

        return toResponse(savedExpense);
    }

    private ExpenseResponse toResponse(Expenses expense) {
        List<SplitResponse> splitResponses = expense.getSplits() != null ?
                expense.getSplits().stream()
                .map(split -> SplitResponse.builder()
                              .participantId(split.getParticipantId())
                              .participantName(split.getParticipantName())
                              .owedAmount(split.getOwedAmount())
                              .shareValue(split.getShareValue())
                              .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        return ExpenseResponse.builder()
                .id(expense.getId())
                .groupId(expense.getGroupId())
                .title(expense.getTitle())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .paidByParticipantId(expense.getPaidByParticipantId())
                .paidByName(expense.getPaidByName())
                .category(expense.getCategory())
                .splitType(expense.getSplitType())
                .expenseDate(expense.getExpenseDate())
                .createdAt(expense.getCreatedAt())
                .splits(splitResponses)
                .build();
    }
}
