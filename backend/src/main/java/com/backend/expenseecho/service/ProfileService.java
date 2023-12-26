package com.backend.expenseecho.service;

import com.backend.expenseecho.model.dto.CreateProfileRequest;
import com.backend.expenseecho.model.dto.ProfileResponse;
import com.backend.expenseecho.model.dto.UpdateProfileRequest;
import com.backend.expenseecho.model.entities.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {
    Profile create(CreateProfileRequest request, String userId);
    List<ProfileResponse> getAllByUserId(String userId);
    ProfileResponse updateProfile(String id, UpdateProfileRequest request, String userId);
}
