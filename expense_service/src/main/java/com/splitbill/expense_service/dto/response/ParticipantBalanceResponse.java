package com.splitbill.expense_service.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParticipantBalanceResponse {

    private UUID participantId;

    private String participantName;

    private BigDecimal totalPaid;

    private BigDecimal totalShare;

    private BigDecimal balance;
}
