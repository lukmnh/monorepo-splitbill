package com.splitbill.expense.service;

import com.splitbill.common.dto.expenses.request.CreateExpenseRequest;
import com.splitbill.common.dto.expenses.response.ExpenseResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ExpenseService {
    ExpenseResponse createExpense(@Valid  @RequestBody Long groupId,CreateExpenseRequest request);
    Page<ExpenseResponse> fetchExpensesByGroup(Long groupId, String category);
    ExpenseResponse detailExpense(Long expenseId);

}
