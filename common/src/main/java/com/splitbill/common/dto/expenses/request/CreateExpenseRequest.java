package com.splitbill.common.dto.expenses.request;

import com.splitbill.common.constant.ExpensesType;
import com.splitbill.common.constant.SplitType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateExpenseRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 150)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @Digits(integer = 13, fraction = 2, message = "Amount format invalid")
    private BigDecimal amount;

    @NotNull(message = "Payer participant ID is required")
    private Long paidByParticipantId;

    @NotBlank(message = "Payer name is required")
    private String paidByName;

    @NotNull(message = "Category is required")
    private ExpensesType category;

    @NotNull(message = "Split type is required")
    private SplitType splitType;

    private LocalDate expenseDate;

    @NotEmpty(message = "At least one participant split is required")
    @Valid
    private List<CreateSplitRequest> splits;
}
