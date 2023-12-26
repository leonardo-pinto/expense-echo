package com.backend.expenseecho.service;

import com.backend.expenseecho.model.entities.Profile;
import com.backend.expenseecho.model.entities.User;

import java.util.List;

public interface UserService {
    User register(User user);
    Boolean emailAlreadyExists(String email);
}
