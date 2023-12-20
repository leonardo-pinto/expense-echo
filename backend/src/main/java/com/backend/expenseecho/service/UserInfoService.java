package com.backend.expenseecho.service;

import com.backend.expenseecho.model.entities.UserInfo;

public interface UserInfoService {
    UserInfo register(UserInfo user);
    Boolean emailAlreadyExists(String email);
}
