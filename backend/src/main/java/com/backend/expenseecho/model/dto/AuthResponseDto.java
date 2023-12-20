package com.backend.expenseecho.model.dto;

public class AuthResponseDto {
    private String accessToken;

    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
