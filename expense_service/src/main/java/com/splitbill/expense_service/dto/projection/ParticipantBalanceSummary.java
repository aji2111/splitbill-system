package com.splitbill.expense_service.dto.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface ParticipantBalanceSummary {

    UUID getParticipantId();

    String getParticipantName();

    BigDecimal getTotalPaid();

    BigDecimal getTotalShare();

    BigDecimal getBalance();
}