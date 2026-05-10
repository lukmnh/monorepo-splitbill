package com.splitbill.common.dto.expenses.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SplitResponse {
    private Long participantId;
    private String participantName;
    private BigDecimal owedAmount;
    private BigDecimal shareValue;
}
