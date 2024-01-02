package com.backend.expenseecho.controller;


import com.backend.expenseecho.model.dto.Profile.CreateProfileRequest;
import com.backend.expenseecho.model.dto.Profile.ProfileResponse;
import com.backend.expenseecho.model.dto.Profile.UpdateProfileRequest;
import com.backend.expenseecho.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ProfileResponse>> getAllByUserId() {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        List<ProfileResponse> response = profileService.getAllByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody CreateProfileRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        ProfileResponse response = profileService.create(request, userId);
        return new ResponseEntity<>(response,  HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable(name = "id") String id, @Valid @RequestBody UpdateProfileRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        ProfileResponse response = profileService.updateProfile(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
