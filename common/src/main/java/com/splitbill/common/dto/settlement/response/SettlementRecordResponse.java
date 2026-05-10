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
public class SettlementRecordResponse {
    private Long id;
    private Long groupId;
    private String groupName;
    private BigDecimal totalExpenses;
    private Integer participantCount;
    private Integer transactionCount;
}
