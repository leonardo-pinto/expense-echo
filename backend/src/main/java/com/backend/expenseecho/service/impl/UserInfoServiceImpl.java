package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.model.entities.UserInfo;
import com.backend.expenseecho.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfo login(UserInfo user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
