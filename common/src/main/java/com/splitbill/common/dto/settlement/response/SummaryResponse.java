package com.splitbill.common.dto.settlement.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryResponse {
    private Long groupId;
    private String groupName;
    private BigDecimal totalExpenses;
    private Integer participantCount;
    private Integer transactionCount;
    private List<ParticipantBalanceResponse> balances;
    private List<SettlementDebtResponse> settlements;
    private List<CategoryStatResponse> categoryStats;
    private LocalDateTime calculatedAt;
}
