package com.backend.expenseecho.service;

import com.backend.expenseecho.model.dto.Transaction.CreateTransactionRequest;
import com.backend.expenseecho.model.dto.Transaction.TransactionResponse;
import com.backend.expenseecho.model.dto.Transaction.TransactionResultByDateAndTypeResponse;
import com.backend.expenseecho.model.dto.Transaction.UpdateTransactionRequest;

import java.time.YearMonth;
import java.util.List;

public interface TransactionService {
    TransactionResponse create(CreateTransactionRequest request, String userId);
    TransactionResponse update(String id, UpdateTransactionRequest request, String userId);
    TransactionResponse getById(String id, String userId);
    List<TransactionResponse> getTransactionsByDateAndBudget(YearMonth date, int budgetId, String userId);
    TransactionResultByDateAndTypeResponse getTransactionsResultByDateAndBudget(YearMonth date, int budgetId, String userId);
}
