package com.splitbill.expense_service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ExpenseSummaryResponse {

    private UUID groupId;

    private String groupName;

    private BigDecimal totalExpense;

    private List<ParticipantBalanceResponse> participants;
}