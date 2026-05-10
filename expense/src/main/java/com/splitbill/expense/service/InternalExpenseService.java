package com.splitbill.expense.service;

import com.splitbill.common.dto.expenses.response.CategorySummaryResponse;
import com.splitbill.common.dto.expenses.response.OwedSummaryResponse;
import com.splitbill.common.dto.expenses.response.PaidSummaryResponse;

import java.util.List;

public interface InternalExpenseService {
    List<PaidSummaryResponse> getPaidSummary(Long groupId);
    List<OwedSummaryResponse> getOwedSummary(Long groupId);
    List<CategorySummaryResponse> getCategorySummary(Long groupId);
}
