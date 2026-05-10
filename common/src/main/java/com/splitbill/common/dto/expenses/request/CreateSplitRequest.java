package com.splitbill.common.dto.expenses.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSplitRequest {
    @NotNull(message = "Participant ID is required")
    private Long participantId;
    @NotBlank(message = "Participant name is required")
    private String participantName;
    private BigDecimal shareValue;
}
