package com.backend.expenseecho.service;

import com.backend.expenseecho.model.dto.Profile.CreateProfileRequest;
import com.backend.expenseecho.model.dto.Profile.ProfileResponse;
import com.backend.expenseecho.model.dto.Profile.UpdateProfileRequest;

import java.util.List;

public interface ProfileService {
    ProfileResponse create(CreateProfileRequest request, String userId);
    List<ProfileResponse> getAllByUserId(String userId);
    ProfileResponse updateProfile(String id, UpdateProfileRequest request, String userId);
}
