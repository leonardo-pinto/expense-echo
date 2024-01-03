package com.backend.expenseecho.model.dto.Transaction;

import com.backend.expenseecho.model.enums.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

public class TransactionResultByDateAndType {
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public TransactionResultByDateAndType(BigDecimal totalAmount, TransactionType type) {
        this.totalAmount = totalAmount;
        this.type = type;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
