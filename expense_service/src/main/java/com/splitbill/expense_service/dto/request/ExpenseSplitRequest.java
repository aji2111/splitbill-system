package com.splitbill.expense_service.dto.request;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseSplitRequest {

    private UUID participantId;

    private BigDecimal amount;

    private BigDecimal percentage;
}
