package com.splitbill.expense.service;

import com.splitbill.common.constant.SplitType;
import com.splitbill.common.dto.expenses.request.CreateSplitRequest;
import com.splitbill.expense.entity.ExpenseSplits;

import java.math.BigDecimal;
import java.util.List;

public interface SplitExpenseService {
    List<ExpenseSplits> calculateSplits(BigDecimal totalAmount,
                                        SplitType splitType,
                                        List<CreateSplitRequest> splitRequests);
}
