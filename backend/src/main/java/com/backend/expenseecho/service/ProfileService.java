package com.backend.expenseecho.service;

import com.backend.expenseecho.model.entities.Profile;

import java.util.List;

public interface ProfileService {
    Profile createProfile(Profile profile);
    List<Profile> getAllProfilesByUserId(String userId);
    Profile getProfileById(int profileId, String userId);
}
