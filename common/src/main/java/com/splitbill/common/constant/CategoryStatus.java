package com.splitbill.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public enum CategoryStatus {
    CREDITOR("Creditor"),
    DEBTOR("Debtor"),
    SETTLED("Settled");

    private final String desc;

    public static CategoryStatus fromNetBalance(BigDecimal net) {
        if (net == null) return SETTLED;
        return switch (net.signum()) {
            case 1  -> CREDITOR;
            case -1 -> DEBTOR;
            default -> SETTLED;
        };
    }
}
