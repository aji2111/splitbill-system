package com.splitbill.expense_service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ExpenseResponse {

    private UUID publicId;

    private String title;

    private BigDecimal totalAmount;

    private String paidBy;

    private LocalDateTime createdAt;
}
