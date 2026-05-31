package com.splitbill.settlement_service.calculator;

import org.springframework.stereotype.Component;

import com.splitbill.settlement_service.dto.response.ParticipantBalanceResponse;
import com.splitbill.settlement_service.dto.response.SettlementItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class SettlementCalculator {

    public List<SettlementItem> calculate(
            List<ParticipantBalanceResponse> balances
    ) {

        List<ParticipantBalanceResponse> creditors =
                balances.stream()
                        .filter(balance ->
                                balance.getBalance()
                                        .compareTo(BigDecimal.ZERO) > 0
                        )
                        .toList();

        List<ParticipantBalanceResponse> debtors =
                balances.stream()
                        .filter(balance ->
                                balance.getBalance()
                                        .compareTo(BigDecimal.ZERO) < 0
                        )
                        .toList();

        List<SettlementItem> settlements =
                new ArrayList<>();

        int creditorIndex = 0;

        for (ParticipantBalanceResponse debtor : debtors) {

            BigDecimal debt =
                    debtor.getBalance().abs();

            while (
                    debt.compareTo(BigDecimal.ZERO) > 0
                            && creditorIndex < creditors.size()
            ) {

                ParticipantBalanceResponse creditor =
                        creditors.get(creditorIndex);

                BigDecimal credit =
                        creditor.getBalance();

                BigDecimal amount =
                        debt.min(credit);

                settlements.add(
                        SettlementItem.builder()

                                .from(
                                        debtor.getParticipantName()
                                )

                                .to(
                                        creditor.getParticipantName()
                                )

                                .amount(
                                        amount.setScale(
                                                2,
                                                RoundingMode.HALF_UP
                                        )
                                )

                                .build()
                );

                debt = debt.subtract(amount);

                creditor.setBalance(
                        credit.subtract(amount)
                );

                if (
                        creditor.getBalance()
                                .compareTo(BigDecimal.ZERO) == 0
                ) {
                    creditorIndex++;
                }
            }
        }

        return settlements;
    }
}