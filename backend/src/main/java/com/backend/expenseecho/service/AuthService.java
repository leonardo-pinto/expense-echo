package com.backend.expenseecho.service;

import com.backend.expenseecho.model.dto.Auth.RegisterRequest;
import com.backend.expenseecho.model.dto.Auth.RegisterUserResponse;

public interface AuthService {
    RegisterUserResponse register(RegisterRequest registerRequest);
    Boolean emailAlreadyExists(String email);
}
