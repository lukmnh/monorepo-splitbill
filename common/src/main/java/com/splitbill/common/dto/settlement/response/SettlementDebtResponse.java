package com.splitbill.common.dto.settlement.response;

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
public class SettlementDebtResponse {
    private Long fromParticipantId;
    private String fromName;
    private Long toParticipantId;
    private String toName;
    private BigDecimal amount;
}
