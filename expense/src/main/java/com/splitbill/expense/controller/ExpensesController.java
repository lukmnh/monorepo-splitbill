package com.splitbill.expense.controller;

import com.splitbill.common.dto.expenses.request.CreateExpenseRequest;
import com.splitbill.common.dto.expenses.response.ExpenseResponse;
import com.splitbill.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/api/{groupId}/expense")
@RequiredArgsConstructor
public class ExpensesController {
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@PathVariable("groupId") Long groupId, @Valid @RequestBody CreateExpenseRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.createExpense(groupId, request));
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> fetchExpense(
            @PathVariable Long groupId,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(expenseService.fetchExpensesByGroup(groupId, category));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> detailExpense(
            @PathVariable Long groupId,
            @PathVariable Long expenseId) {
        return ResponseEntity.ok(expenseService.detailExpense(expenseId));
    }
}
