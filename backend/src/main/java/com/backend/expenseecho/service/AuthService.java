package com.backend.expenseecho.service;

import com.backend.expenseecho.model.dto.RegisterRequest;
import com.backend.expenseecho.model.dto.RegisterUserResponse;
import com.backend.expenseecho.model.entities.User;

public interface AuthService {
    RegisterUserResponse register(RegisterRequest registerRequest);
    Boolean emailAlreadyExists(String email);
}
