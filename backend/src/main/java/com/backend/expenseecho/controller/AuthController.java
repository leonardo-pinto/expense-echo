package com.backend.expenseecho.controller;

import com.backend.expenseecho.security.UserInfoUserDetails;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.model.dto.AuthResponse;
import com.backend.expenseecho.model.dto.LoginRequest;
import com.backend.expenseecho.model.dto.RegisterRequest;
import com.backend.expenseecho.security.JwtTokenProvider;
import com.backend.expenseecho.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userInfoService;
    private final JwtTokenProvider jwtService;
    private final AuthenticationManager authManager;

    public AuthController(UserService userInfoService, JwtTokenProvider jwtService, AuthenticationManager authManager) {
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // TODO: map dto to entity
        User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());
        User createdUser = userInfoService.register(user);
        String token = jwtService.generateToken(createdUser.getEmail(), createdUser.getId().toString());
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
