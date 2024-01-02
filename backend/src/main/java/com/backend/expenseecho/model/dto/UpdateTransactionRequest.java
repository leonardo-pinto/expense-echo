package com.backend.expenseecho.model.dto;

import com.backend.expenseecho.model.enums.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public class UpdateTransactionRequest {
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

    @NotNull(message = "Transaction date is required")
    private Date date;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
