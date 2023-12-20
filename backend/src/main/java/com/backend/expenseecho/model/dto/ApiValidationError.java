package com.backend.expenseecho.model.dto;

public class ApiValidationError extends ApiSubError {
    private String message;

    public ApiValidationError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
