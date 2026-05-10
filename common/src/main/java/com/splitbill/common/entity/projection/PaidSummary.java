package com.splitbill.common.entity.projection;

import java.math.BigDecimal;

public interface PaidSummary {
    String getGroupName();
    Long getParticipantId();
    String getParticipantName();
    BigDecimal getTotalPaid();
    Long getExpenseCount();
}
