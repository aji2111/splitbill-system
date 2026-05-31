package com.splitbill.expense_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitbill.expense_service.dto.request.AddExpenseRequest;
import com.splitbill.expense_service.dto.response.BaseResponse;
import com.splitbill.expense_service.dto.response.ExpenseResponse;
import com.splitbill.expense_service.dto.response.ExpenseSummaryResponse;
import com.splitbill.expense_service.service.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/groups")

@RequiredArgsConstructor

@Tag(
        name = "Expense API",
        description = "API for split bill expenses"
)
public class ExpenseController {

    private final ExpenseService expenseService;

    @Operation(
            summary = "Add expense into group"
    )

    @PostMapping(
            "/{groupPublicId}/expenses"
    )

    public ResponseEntity<
            BaseResponse<ExpenseResponse>
            >

    addExpense(

            @PathVariable
            UUID groupPublicId,

            @Valid

            @RequestBody
            AddExpenseRequest request
    ) {

        ExpenseResponse expenseResponse =

                expenseService.addExpense(
                        groupPublicId,
                        request
                );

        BaseResponse<ExpenseResponse> response =

                BaseResponse
                        .<ExpenseResponse>builder()

                        .message(
                                "Expense created successfully"
                        )

                        .data(
                                expenseResponse
                        )

                        .build();

        return ResponseEntity.ok(
                response
        );
    }
   @GetMapping("/{groupPublicId}/summary")
    public ExpenseSummaryResponse getSummary(
            @PathVariable UUID groupPublicId
    ) {

        return expenseService.getGroupSummary(
                groupPublicId
        );
    }
}