package com.splitbill.expense_service.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.splitbill.expense_service.constant.SplitType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddExpenseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Total amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Total amount must be greater than zero"
    )
    private BigDecimal totalAmount;

    @NotNull(message = "Paid by participant is required")
    private UUID paidByParticipantId;

    @NotNull(message = "Split type is required")
    private SplitType splitType;

    @Valid
    @NotEmpty(message = "Splits cannot be empty")
    private List<ExpenseSplitRequest> splits;
}