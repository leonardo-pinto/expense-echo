package com.backend.expenseecho.controller;

import com.backend.expenseecho.model.dto.CreateTransactionRequest;
import com.backend.expenseecho.model.dto.TransactionResponse;
import com.backend.expenseecho.model.dto.TransactionResultByDateAndTypeResponse;
import com.backend.expenseecho.model.dto.UpdateTransactionRequest;
import com.backend.expenseecho.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody CreateTransactionRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        TransactionResponse response = transactionService.create(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TransactionResponse> update(@PathVariable String id, @Valid @RequestBody UpdateTransactionRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        TransactionResponse response = transactionService.update(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TransactionResponse> getById(@PathVariable String id) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        TransactionResponse response = transactionService.getById(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/budget/{budgetId}/date/{date}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByBudgetAndDate(@PathVariable int budgetId, @PathVariable YearMonth date) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        List<TransactionResponse> response = transactionService.getTransactionsByDateAndBudget(date, budgetId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("monthly-result/budget/{budgetId}/date/{date}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<TransactionResultByDateAndTypeResponse> getResultByBudgetAndDate(@PathVariable int budgetId, @PathVariable YearMonth date) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        TransactionResultByDateAndTypeResponse response = transactionService.getTransactionsResultByDateAndBudget(date, budgetId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
