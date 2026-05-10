package com.splitbill.expense.service;

import com.splitbill.common.dto.expenses.request.CreateExpenseRequest;
import com.splitbill.common.dto.expenses.response.ExpenseResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface ExpenseService {
    ExpenseResponse createExpense(@Valid  @RequestBody Long groupId,CreateExpenseRequest request);
}
