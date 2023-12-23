package com.backend.expenseecho.model.dto;

import com.backend.expenseecho.model.entities.Profile;

public class ProfileResponse {
    private Integer profileId;
    private String name;
    private String avatar;

    public ProfileResponse(Profile profile) {
        this.profileId = profile.getId();
        this.name = profile.getName();
        this.avatar = profile.getAvatar();
    }

    public Integer getProfileId() {
        return profileId;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
