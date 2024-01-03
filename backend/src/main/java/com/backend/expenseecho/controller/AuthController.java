package com.backend.expenseecho.controller;

import com.backend.expenseecho.model.dto.Auth.RegisterUserResponse;
import com.backend.expenseecho.security.UserInfoUserDetails;
import com.backend.expenseecho.model.dto.Auth.AuthResponse;
import com.backend.expenseecho.model.dto.Auth.LoginRequest;
import com.backend.expenseecho.model.dto.Auth.RegisterRequest;
import com.backend.expenseecho.security.JwtTokenProvider;
import com.backend.expenseecho.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService userInfoService;
    private final JwtTokenProvider jwtService;
    private final AuthenticationManager authManager;

    public AuthController(AuthService userInfoService, JwtTokenProvider jwtService, AuthenticationManager authManager) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterUserResponse registerUser = userInfoService.register(request);
        String token = jwtService.generateToken(registerUser.getEmail(), registerUser.getId().toString());
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserInfoUserDetails userDetails = (UserInfoUserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails.getUsername(), userDetails.getId().toString());
            return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }
}
