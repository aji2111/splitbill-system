package com.splitbill.settlement_service.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantBalanceResponse {

    private UUID participantId;

    private String participantName;

    private BigDecimal totalPaid;

    private BigDecimal totalShare;

    private BigDecimal balance;
}