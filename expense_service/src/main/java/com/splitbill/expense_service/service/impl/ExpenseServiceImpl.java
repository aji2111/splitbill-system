package com.splitbill.expense_service.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.splitbill.expense_service.constant.SplitType;
import com.splitbill.expense_service.dto.projection.ParticipantBalanceSummary;
import com.splitbill.expense_service.dto.request.AddExpenseRequest;
import com.splitbill.expense_service.dto.request.ExpenseSplitRequest;
import com.splitbill.expense_service.dto.response.ExpenseResponse;
import com.splitbill.expense_service.dto.response.ExpenseSummaryResponse;
import com.splitbill.expense_service.dto.response.ParticipantBalanceResponse;
import com.splitbill.expense_service.entity.BillGroup;
import com.splitbill.expense_service.entity.Expense;
import com.splitbill.expense_service.entity.ExpenseSplit;
import com.splitbill.expense_service.entity.Participant;
import com.splitbill.expense_service.repository.BillGroupRepository;
import com.splitbill.expense_service.repository.ExpenseRepository;
import com.splitbill.expense_service.repository.ParticipantRepository;
import com.splitbill.expense_service.service.ExpenseService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final BillGroupRepository
            billGroupRepository;

    private final ParticipantRepository
            participantRepository;

    @Override
    public ExpenseResponse addExpense(
            UUID groupPublicId,
            AddExpenseRequest request
    ) {

        log.info(
                "Creating expense for groupId={}",
                groupPublicId
        );

        validateRequest(request);

        BillGroup group =
                findGroupByPublicId(groupPublicId);

        Participant paidBy =
                findParticipantByPublicId(
                        request.getPaidByParticipantId()
                );

        Expense expense =
                buildExpense(
                        request,
                        group,
                        paidBy
                );

        processExpenseSplits(
                expense,
                request
        );

        Expense savedExpense =
                expenseRepository.save(expense);

        log.info(
                "Expense created successfully. expenseId={}",
                savedExpense.getPublicId()
        );

        return mapToResponse(savedExpense);
    }

  @Override
    public ExpenseSummaryResponse getGroupSummary(
            UUID groupPublicId
    ) {

        BillGroup group =
                billGroupRepository
                        .findByPublicId(groupPublicId)
                        .orElseThrow();

        List<ParticipantBalanceResponse> participants =
                expenseRepository
                        .getParticipantBalances(groupPublicId)
                        .stream()
                        .map(this::mapParticipant)
                        .toList();

        BigDecimal totalExpense =
                participants.stream()
                        .map(
                                ParticipantBalanceResponse::getTotalPaid
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return ExpenseSummaryResponse.builder()

                .groupId(
                        group.getPublicId()
                )

                .groupName(
                        group.getName()
                )

                .totalExpense(
                        totalExpense
                )

                .participants(
                        participants
                )

                .build();
    }

    private ParticipantBalanceResponse mapParticipant(
            ParticipantBalanceSummary balance
    ) {

        return ParticipantBalanceResponse.builder()

                .participantId(
                        balance.getParticipantId()
                )

                .participantName(
                        balance.getParticipantName()
                )

                .totalPaid(
                        balance.getTotalPaid()
                )

                .totalShare(
                        balance.getTotalShare()
                )

                .balance(
                        balance.getBalance()
                )

                .build();
    }


    private void validateRequest(
            AddExpenseRequest request
    ) {

        validateSplitRequest(request);
    }

    private BillGroup findGroupByPublicId(
            UUID groupPublicId
    ) {

        return billGroupRepository
                .findByPublicId(groupPublicId)
                .orElseThrow(() ->

                        new EntityNotFoundException(
                                "Group not found with id: "
                                        + groupPublicId
                        )
                );
    }

    private Participant findParticipantByPublicId(
            UUID participantPublicId
    ) {

        return participantRepository
                .findByPublicId(participantPublicId)
                .orElseThrow(() ->

                        new EntityNotFoundException(
                                "Participant not found with id: "
                                        + participantPublicId
                        )
                );
    }

    private Expense buildExpense(
            AddExpenseRequest request,
            BillGroup group,
            Participant paidBy
    ) {

        Expense expense = new Expense();

        expense.setGroup(group);
        expense.setPaidBy(paidBy);
        expense.setTitle(request.getTitle());
        expense.setDescription(
                request.getDescription()
        );
        expense.setTotal_amount(
                request.getTotalAmount()
        );
        expense.setSplitType(
                request.getSplitType()
        );

        return expense;
    }

    private void processExpenseSplits(
            Expense expense,
            AddExpenseRequest request
    ) {

        switch (request.getSplitType()) {

            case EQUAL ->
                    handleEqualSplit(
                            expense,
                            request
                    );

            case EXACT ->
                    handleExactSplit(
                            expense,
                            request
                    );

            case PERCENTAGE ->
                    handlePercentageSplit(
                            expense,
                            request
                    );

            default ->
                    throw new IllegalArgumentException(
                            """
                            Invalid split type.

                            Allowed values:
                            EQUAL,
                            EXACT,
                            PERCENTAGE
                            """
                    );
        }
    }

    private void validateSplitRequest(
            AddExpenseRequest request
    ) {

        if (
                request.getSplits() == null
                || request.getSplits().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    """
                    Split request cannot be empty.

                    Please provide
                    at least one participant.
                    """
            );
        }

        request.getSplits()
                .forEach(split ->

                        validateSplitByType(
                                request.getSplitType(),
                                split
                        )
                );
    }

    private void validateSplitByType(
            SplitType splitType,
            ExpenseSplitRequest split
    ) {

        switch (splitType) {

            case EQUAL ->
                    validateEqualSplit(split);

            case EXACT ->
                    validateExactSplit(split);

            case PERCENTAGE ->
                    validatePercentageSplit(split);
        }
    }

    private void validateEqualSplit(
            ExpenseSplitRequest split
    ) {

        boolean hasAmount =
                split.getAmount() != null
                        && split.getAmount()
                        .compareTo(BigDecimal.ZERO) > 0;

        boolean hasPercentage =
                split.getPercentage() != null
                        && split.getPercentage()
                        .compareTo(BigDecimal.ZERO) > 0;

        if (
                hasAmount
                || hasPercentage
        ) {

            throw new IllegalArgumentException(
                    """
                    Invalid EQUAL split request.

                    Amount and percentage
                    must be empty or zero
                    because EQUAL split
                    is automatically calculated.

                    ParticipantId:
                    """
                            + split.getParticipantId()
            );
        }
    }

    private void validateExactSplit(
            ExpenseSplitRequest split
    ) {

        if (
                split.getAmount() == null
        ) {

            throw new IllegalArgumentException(
                    """
                    Invalid EXACT split request.

                    Amount is required
                    for EXACT split.

                    ParticipantId:
                    """
                            + split.getParticipantId()
            );
        }

        if (
                split.getAmount()
                        .compareTo(BigDecimal.ZERO) <= 0
        ) {

            throw new IllegalArgumentException(
                    """
                    Invalid EXACT split request.

                    Amount must be
                    greater than zero.

                    ParticipantId:
                    """
                            + split.getParticipantId()
            );
        }

        boolean hasPercentage =
                split.getPercentage() != null
                        && split.getPercentage()
                        .compareTo(BigDecimal.ZERO) > 0;

        if (hasPercentage) {

            throw new IllegalArgumentException(
                    """
                    Invalid EXACT split request.

                    Percentage must be empty
                    or zero for EXACT split.

                    ParticipantId:
                    """
                            + split.getParticipantId()
            );
        }
    }

    private void validatePercentageSplit(
            ExpenseSplitRequest split
    ) {

        if (
                split.getPercentage() == null
        ) {

            throw new IllegalArgumentException(
                    """
                    Invalid PERCENTAGE split request.

                    Percentage is required
                    for PERCENTAGE split.

                    ParticipantId:
                    """
                            + split.getParticipantId()
            );
        }

        if (
                split.getPercentage()
                        .compareTo(BigDecimal.ZERO) <= 0
        ) {

            throw new IllegalArgumentException(
                    """
                    Invalid PERCENTAGE split request.

                    Percentage must be
                    greater than zero.

                    ParticipantId:
                    """
                            + split.getParticipantId()
            );
        }

        if (
                split.getPercentage()
                        .compareTo(BigDecimal.valueOf(100)) > 0
        ) {

            throw new IllegalArgumentException(
                    """
                    Invalid PERCENTAGE split request.

                    Percentage cannot
                    exceed 100.

                    ParticipantId:
                    """
                            + split.getParticipantId()
            );
        }

        boolean hasAmount =
                split.getAmount() != null
                        && split.getAmount()
                        .compareTo(BigDecimal.ZERO) > 0;

        if (hasAmount) {

            throw new IllegalArgumentException(
                    """
                    Invalid PERCENTAGE split request.

                    Amount must be empty
                    or zero for PERCENTAGE split.

                    ParticipantId:
                    """
                            + split.getParticipantId()
            );
        }
    }

    private void handleEqualSplit(
            Expense expense,
            AddExpenseRequest request
    ) {

        BigDecimal splitAmount =
                calculateEqualAmount(
                        expense.getTotal_amount(),
                        request.getSplits().size()
                );

        request.getSplits()
                .forEach(split ->

                        addExpenseSplit(
                                expense,
                                split.getParticipantId(),
                                splitAmount
                        )
                );
    }

    private void handleExactSplit(
            Expense expense,
            AddExpenseRequest request
    ) {

        BigDecimal totalSplit =
                BigDecimal.ZERO;

        for (
                ExpenseSplitRequest split :
                request.getSplits()
        ) {

            addExpenseSplit(
                    expense,
                    split.getParticipantId(),
                    split.getAmount()
            );

            totalSplit =
                    totalSplit.add(
                            split.getAmount()
                    );
        }

        validateExactTotal(
                expense.getTotal_amount(),
                totalSplit
        );
    }

    private void handlePercentageSplit(
            Expense expense,
            AddExpenseRequest request
    ) {

        BigDecimal totalPercentage =
                BigDecimal.ZERO;

        for (
                ExpenseSplitRequest split :
                request.getSplits()
        ) {

            BigDecimal splitAmount =
                    calculatePercentageAmount(
                            expense.getTotal_amount(),
                            split.getPercentage()
                    );

            addExpenseSplit(
                    expense,
                    split.getParticipantId(),
                    splitAmount
            );

            totalPercentage =
                    totalPercentage.add(
                            split.getPercentage()
                    );
        }

        validatePercentageTotal(
                totalPercentage
        );
    }

    private void addExpenseSplit(
            Expense expense,
            UUID participantId,
            BigDecimal shareAmount
    ) {

        Participant participant =
                findParticipantByPublicId(
                        participantId
                );

        ExpenseSplit split =
                new ExpenseSplit();

        split.setExpense(expense);
        split.setParticipant(participant);
        split.setShareAmount(shareAmount);

        expense.getSplits().add(split);
    }

    private void validateExactTotal(
            BigDecimal expenseAmount,
            BigDecimal totalSplit
    ) {

        if (
                totalSplit.compareTo(
                        expenseAmount
                ) != 0
        ) {

            throw new IllegalArgumentException(
                    """
                    Invalid EXACT split request.

                    Total split amount
                    must exactly match
                    expense amount.

                    Expense Amount:
                    """
                            + expenseAmount

                            + """

                    Total Split:
                    """
                            + totalSplit
            );
        }
    }

    private void validatePercentageTotal(
            BigDecimal totalPercentage
    ) {

        if (
                totalPercentage.compareTo(
                        BigDecimal.valueOf(100)
                ) != 0
        ) {

            throw new IllegalArgumentException(
                    """
                    Invalid PERCENTAGE split request.

                    Total percentage
                    must equal exactly 100.

                    Current Total:
                    """
                            + totalPercentage
            );
        }
    }

    private BigDecimal calculateEqualAmount(
            BigDecimal amount,
            int participantSize
    ) {

        return amount.divide(
                BigDecimal.valueOf(participantSize),
                2,
                RoundingMode.HALF_UP
        );
    }

    private BigDecimal calculatePercentageAmount(
            BigDecimal amount,
            BigDecimal percentage
    ) {

        return amount.multiply(percentage)
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private ExpenseResponse mapToResponse(
            Expense expense
    ) {

        return ExpenseResponse.builder()
                .publicId(
                        expense.getPublicId()
                )
                .title(
                        expense.getTitle()
                )
                .totalAmount(
                        expense.getTotal_amount()
                )
                .paidBy(
                        expense.getPaidBy().getName()
                )
                .createdAt(
                        expense.getCreatedAt()
                )
                .build();
    }
    
}

