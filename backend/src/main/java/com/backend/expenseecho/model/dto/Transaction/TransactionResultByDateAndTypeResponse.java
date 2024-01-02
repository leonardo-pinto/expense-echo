package com.backend.expenseecho.model.dto.Transaction;

import java.math.BigDecimal;

public class TransactionResultByDateAndTypeResponse {
    private BigDecimal totalResult;
    private BigDecimal totalExpense;
    private BigDecimal totalIncome;

    public TransactionResultByDateAndTypeResponse() {}

    public TransactionResultByDateAndTypeResponse(BigDecimal totalExpense, BigDecimal totalIncome) {
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
        this.totalResult = totalIncome.subtract(totalExpense);
    }

    public BigDecimal getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(BigDecimal totalResult) {
        this.totalResult = totalResult;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

}
