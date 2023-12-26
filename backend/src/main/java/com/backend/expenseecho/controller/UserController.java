package com.backend.expenseecho.controller;


import com.backend.expenseecho.model.dto.ProfileResponse;
import com.backend.expenseecho.service.ProfileService;
import com.backend.expenseecho.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private ProfileService profileService;

    public UserController(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }


    @GetMapping("/profiles")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ProfileResponse>> getAllProfilesByUser() {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        List<ProfileResponse> response = profileService.getAllByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
