package com.splitbill.settlement_service.dto.response;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettlementResponse {

    private UUID groupId;

    private String groupName;

    private BigDecimal totalExpense;

    private List<SettlementItem> settlements;
}