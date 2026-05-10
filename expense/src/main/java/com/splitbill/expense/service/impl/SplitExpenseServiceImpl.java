package com.splitbill.expense.service.impl;

import com.splitbill.common.constant.ErrorCode;
import com.splitbill.common.constant.SplitType;
import com.splitbill.common.dto.expenses.request.CreateSplitRequest;
import com.splitbill.common.exception.BusinessException;
import com.splitbill.expense.entity.ExpenseSplits;
import com.splitbill.expense.service.SplitExpenseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SplitExpenseServiceImpl implements SplitExpenseService {
    @Override
    public List<ExpenseSplits> calculateSplits(BigDecimal totalAmount, SplitType splitType, List<CreateSplitRequest> splitRequests) {
        return switch (splitType) {
            case EQUAL -> calculateEqualSplits(totalAmount, splitRequests);
            case EXACT -> calculateExactSplits(totalAmount, splitRequests);
        };
    }

    private List<ExpenseSplits> calculateEqualSplits(BigDecimal total, List<CreateSplitRequest> requests) {
        int count = requests.size();
        BigDecimal equalShare = total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        BigDecimal lastShare = total.subtract(equalShare.multiply(BigDecimal.valueOf(count - 1)));

        List<ExpenseSplits> splits = requests.stream()
                .limit(count - 1)
                .map(req -> ExpenseSplits.builder()
                        .participantId(req.getParticipantId())
                        .participantName(req.getParticipantName())
                        .owedAmount(equalShare)
                        .shareValue(null)
                        .build())
                .collect(Collectors.toList());

        CreateSplitRequest last = requests.get(count - 1);
        splits.add(ExpenseSplits.builder()
                .participantId(last.getParticipantId())
                .participantName(last.getParticipantName())
                .owedAmount(lastShare)
                .shareValue(null)
                .build());

        return splits;
    }

    private List<ExpenseSplits> calculateExactSplits(BigDecimal total, List<CreateSplitRequest> requests) {
        BigDecimal sum = requests.stream()
                .map(r -> r.getShareValue() != null ? r.getShareValue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(total) != 0) {
            throw new BusinessException(
                    ErrorCode.INVALID_SPLIT_AMOUNT.getCode(),
                    ErrorCode.INVALID_SPLIT_AMOUNT.getMessage() +
                            String.format(" (Expected: %.2f, Actual: %.2f)", total, sum),
                    ErrorCode.INVALID_SPLIT_AMOUNT.getHttpStatus()
            );
        }

        return requests.stream()
                .map(req -> ExpenseSplits.builder()
                        .participantId(req.getParticipantId())
                        .participantName(req.getParticipantName())
                        .owedAmount(req.getShareValue())
                        .shareValue(req.getShareValue())
                        .build())
                .collect(Collectors.toList());
    }
}
