package com.splitbill.settlement.client;

import com.splitbill.common.dto.expenses.response.CategorySummaryResponse;
import com.splitbill.common.dto.expenses.response.OwedSummaryResponse;
import com.splitbill.common.dto.expenses.response.PaidSummaryResponse;
import com.splitbill.settlement.config.ServiceClientConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ExpenseServiceClient {
        private final RestTemplate restTemplate;
        private final ServiceClientConfiguration serviceClientConfiguration;

        public ExpenseServiceClient(RestTemplate restTemplate, ServiceClientConfiguration serviceClientConfiguration) {
            this.restTemplate = restTemplate;
            this.serviceClientConfiguration = serviceClientConfiguration;
        }

        public List<PaidSummaryResponse> getPaidSummary(Long groupId) {
            String url = serviceClientConfiguration.getExpenseService().getUrl() + "/internal/v1/expenses/groups/" + groupId + "/paid";

            ResponseEntity<List<PaidSummaryResponse>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<PaidSummaryResponse>>() {});
            return response.getBody();
        }

    public List<OwedSummaryResponse> getOwedSummary(Long groupId) {
        String url = serviceClientConfiguration.getExpenseService().getUrl() + "/internal/v1/expenses/groups/" + groupId + "/owed";
        ResponseEntity<List<OwedSummaryResponse>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<OwedSummaryResponse>>() {});
        return response.getBody();
    }

    public List<CategorySummaryResponse> getCategorySummary(Long groupId) {
        String url = serviceClientConfiguration.getExpenseService().getUrl() + "/internal/v1/expenses/groups/" + groupId + "/categories";
        ResponseEntity<List<CategorySummaryResponse>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CategorySummaryResponse>>() {});
        return response.getBody();
    }

}
