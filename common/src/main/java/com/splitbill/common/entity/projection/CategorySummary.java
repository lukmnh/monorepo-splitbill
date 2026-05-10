package com.splitbill.common.entity.projection;

import java.math.BigDecimal;

public interface CategorySummary {
    String getCategory();
    BigDecimal getTotal();
    Long getCount();
    BigDecimal getAverage();
}
