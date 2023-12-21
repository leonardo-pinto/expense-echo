package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
import com.backend.expenseecho.model.entities.UserInfo;
import com.backend.expenseecho.repository.UserInfoRepository;
import com.backend.expenseecho.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private UserInfoRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserInfoServiceImpl(UserInfoRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserInfo register(UserInfo user) {
        if (emailAlreadyExists(user.getEmail())) {
            throw new BadRequestException("Email already registered.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Boolean emailAlreadyExists(String email) {
        Optional<UserInfo> user = userRepository.findByEmail(email);
        return user.isPresent();
    }
}
