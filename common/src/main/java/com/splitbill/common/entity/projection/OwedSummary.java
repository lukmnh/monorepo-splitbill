package com.splitbill.common.entity.projection;

import java.math.BigDecimal;

public interface OwedSummary {
    Long getParticipantId();
    String getParticipantName();
    BigDecimal getTotalOwed();
    Long getExpenseCount();
}
