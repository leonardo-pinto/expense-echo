package com.backend.expenseecho.controller;

import com.backend.expenseecho.model.dto.Budget.BudgetResponse;
import com.backend.expenseecho.model.dto.Budget.CreateBudgetRequest;
import com.backend.expenseecho.model.dto.Budget.UpdateBudgetRequest;
import com.backend.expenseecho.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {

        this.budgetService = budgetService;
    }

    @GetMapping()
    public ResponseEntity<List<BudgetResponse>> getAllByUserId() {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        List<BudgetResponse> response = budgetService.getAllByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BudgetResponse> createBudget(@Valid @RequestBody CreateBudgetRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        BudgetResponse response = budgetService.create(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(@PathVariable String id, @Valid @RequestBody UpdateBudgetRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        BudgetResponse response = budgetService.update(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        budgetService.delete(id, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
