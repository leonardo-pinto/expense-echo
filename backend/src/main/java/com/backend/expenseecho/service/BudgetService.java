package com.backend.expenseecho.service;

import com.backend.expenseecho.model.dto.Budget.BudgetResponse;
import com.backend.expenseecho.model.dto.Budget.CreateBudgetRequest;
import com.backend.expenseecho.model.dto.Budget.UpdateBudgetRequest;

import java.util.List;

public interface BudgetService {
    BudgetResponse create(CreateBudgetRequest request, String userId);
    List<BudgetResponse> getAllByUserId (String userId);
    BudgetResponse update(String id, UpdateBudgetRequest request, String userId);
    void delete(String id, String userId);
}
