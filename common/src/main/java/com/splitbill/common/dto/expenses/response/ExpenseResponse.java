package com.splitbill.common.dto.expenses.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.splitbill.common.constant.ExpensesType;
import com.splitbill.common.constant.SplitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenseResponse {
    private Long id;
    private Long groupId;
    private String title;
    private String description;
    private BigDecimal amount;
    private Long paidByParticipantId;
    private String paidByName;
    private ExpensesType category;
    private SplitType splitType;
    private LocalDate expenseDate;
    private List<SplitResponse> splits;
    private LocalDateTime createdAt;
}
