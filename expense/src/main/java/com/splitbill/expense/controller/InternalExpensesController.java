package com.splitbill.expense.controller;

import com.splitbill.common.dto.expenses.response.CategorySummaryResponse;
import com.splitbill.common.dto.expenses.response.OwedSummaryResponse;
import com.splitbill.common.dto.expenses.response.PaidSummaryResponse;
import com.splitbill.expense.service.ExpenseService;
import com.splitbill.expense.service.InternalExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/v1/expenses")
@RequiredArgsConstructor
public class InternalExpensesController {
    private final InternalExpenseService  internalExpenseService;

    @GetMapping("/groups/{groupId}/paid")
    public ResponseEntity<List<PaidSummaryResponse>> getPaidSummary(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(internalExpenseService.getPaidSummary(groupId));
    }

    @GetMapping("/groups/{groupId}/owed")
    public ResponseEntity<List<OwedSummaryResponse>> getOwedSummary(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(internalExpenseService.getOwedSummary(groupId));
    }

    @GetMapping("/groups/{groupId}/categories")
    public ResponseEntity<List<CategorySummaryResponse>> getCategorySummary(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(internalExpenseService.getCategorySummary(groupId));
    }

}
