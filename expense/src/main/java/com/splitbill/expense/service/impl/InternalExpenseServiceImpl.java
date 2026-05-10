package com.splitbill.expense.service.impl;

import com.splitbill.common.dto.expenses.response.CategorySummaryResponse;
import com.splitbill.common.dto.expenses.response.OwedSummaryResponse;
import com.splitbill.common.dto.expenses.response.PaidSummaryResponse;
import com.splitbill.expense.repository.ExpensesRepository;
import com.splitbill.expense.service.InternalExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalExpenseServiceImpl implements InternalExpenseService {
    private final ExpensesRepository expensesRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PaidSummaryResponse> getPaidSummary(Long groupId) {
        log.info("inside getPaidSummary() with groupId {}", groupId);
        return expensesRepository.findTotalPaidPerParticipant(groupId)
                .stream()
                .map(row -> PaidSummaryResponse.builder()
                        .groupName(row.getGroupName())
                        .participantId(row.getParticipantId())
                        .participantName(row.getParticipantName())
                        .totalPaid(row.getTotalPaid())
                        .expenseCount(row.getExpenseCount())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwedSummaryResponse> getOwedSummary(Long groupId) {
        log.info("inside getOwedSummary() with groupId {}", groupId);
        return expensesRepository.findTotalOwedPerParticipant(groupId)
                .stream()
                .map(row -> OwedSummaryResponse.builder()
                        .participantId(row.getParticipantId())
                        .participantName(row.getParticipantName())
                        .totalOwed(row.getTotalOwed())
                        .expenseCount(row.getExpenseCount())
                        .build())
                .toList();    }

    @Override
    @Transactional(readOnly = true)
    public List<CategorySummaryResponse> getCategorySummary(Long groupId) {
        log.info("inside getCategorySummary() with groupId {}", groupId);
        return expensesRepository.findExpenseBreakdownByCategory(groupId)
                .stream()
                .map(row -> CategorySummaryResponse.builder()
                        .category(row.getCategory())
                        .total(row.getTotal())
                        .count(row.getCount())
                        .average(row.getAverage())
                        .build())
                .toList();
    }
}
