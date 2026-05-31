package com.splitbill.expense_service.service;


import java.util.UUID;

import com.splitbill.expense_service.dto.request.AddExpenseRequest;
import com.splitbill.expense_service.dto.response.ExpenseResponse;
import com.splitbill.expense_service.dto.response.ExpenseSummaryResponse;



public interface ExpenseService {

    public ExpenseResponse addExpense(
            UUID groupPublicId,
            AddExpenseRequest request
    );
    ExpenseSummaryResponse getGroupSummary(
            UUID groupPublicId
    );
}


