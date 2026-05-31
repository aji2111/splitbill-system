package com.splitbill.settlement_service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ExpenseSummaryResponse {

    private UUID groupId;

    private String groupName;

    private BigDecimal totalExpense;

    private List<ParticipantBalanceResponse> participants;
}