package com.splitbill.settlement_service.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BalanceHolder {

    private UUID userId;

    private String userName;

    private BigDecimal amount;
}