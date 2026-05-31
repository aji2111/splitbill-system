package com.splitbill.settlement_service.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.splitbill.settlement_service.calculator.SettlementCalculator;
import com.splitbill.settlement_service.client.ExpenseClient;

import com.splitbill.settlement_service.dto.response.ExpenseSummaryResponse;
import com.splitbill.settlement_service.dto.response.SettlementResponse;

import com.splitbill.settlement_service.service.SettlementService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl
        implements SettlementService {

    private final ExpenseClient expenseClient;

    private final SettlementCalculator settlementCalculator;

    @Override
    public SettlementResponse generateSettlement(
            UUID groupPublicId
    ) {

        ExpenseSummaryResponse summary =
                expenseClient.getSummary(groupPublicId);

        return SettlementResponse.builder()

                .groupId(
                        summary.getGroupId()
                )

                .groupName(
                        summary.getGroupName()
                )

                .totalExpense(
                        summary.getTotalExpense()
                )

                .settlements(
                        settlementCalculator.calculate(
                                summary.getParticipants()
                        )
                )

                .build();
    }
}