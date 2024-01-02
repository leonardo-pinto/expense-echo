package com.backend.expenseecho.model.dto;

import com.backend.expenseecho.model.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public class CreateTransactionRequest {

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0", message = "Transaction amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Transaction description is required")
    private String description;

    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull(message = "Category is required")
    private Integer categoryId;

    @NotNull(message = "Profile is required")
    private Integer profileId;

    @NotNull(message = "Budget is required")
    private Integer budgetId;

    @NotNull(message = "Transaction date is required")
    private Date date;

    public CreateTransactionRequest(){}

    public CreateTransactionRequest(BigDecimal amount, String description, TransactionType type, Integer categoryId, Integer profileId, Integer budgetId, Date date) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.categoryId = categoryId;
        this.profileId = profileId;
        this.budgetId = budgetId;
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public TransactionType getType() {
        return type;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public Integer getBudgetId() {
        return budgetId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public void setBudgetId(Integer budgetId) {
        this.budgetId = budgetId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
