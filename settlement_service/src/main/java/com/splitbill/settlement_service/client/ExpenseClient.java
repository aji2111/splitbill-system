package com.splitbill.settlement_service.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.splitbill.settlement_service.config.FeignConfig;

import com.splitbill.settlement_service.dto.response.ExpenseSummaryResponse;

import java.util.UUID;

@FeignClient(
        name = "expense-service",
        url = "${expense-service.url}",
        configuration = FeignConfig.class
)
public interface ExpenseClient {

    @GetMapping("/api/v1/groups/{groupPublicId}/summary")
    ExpenseSummaryResponse getSummary(
            @PathVariable UUID groupPublicId
    );
}