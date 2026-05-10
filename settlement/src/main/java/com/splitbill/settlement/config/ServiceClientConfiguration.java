package com.splitbill.settlement.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services")
@Getter
@Setter
public class ServiceClientConfiguration {
    private final ExpenseService expenseService = new ExpenseService();

    @Getter
    @Setter
    public static class ExpenseService {
        private String url;
    }
}
