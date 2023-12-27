package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
import com.backend.expenseecho.model.dto.RegisterRequest;
import com.backend.expenseecho.model.dto.RegisterUserResponse;
import com.backend.expenseecho.model.entities.Profile;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.repository.UserRepository;
import com.backend.expenseecho.service.AuthService;
import com.backend.expenseecho.utils.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterUserResponse register(RegisterRequest registerRequest) {
        User user = UserMapper.INSTANCE.convert(registerRequest);
        if (emailAlreadyExists(user.getEmail())) {
            throw new BadRequestException("Email already registered.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);

        return UserMapper.INSTANCE.convert(createdUser);
    }

    @Override
    public Boolean emailAlreadyExists(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

}
