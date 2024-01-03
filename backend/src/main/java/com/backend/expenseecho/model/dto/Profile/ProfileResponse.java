package com.backend.expenseecho.model.dto.Profile;

import com.backend.expenseecho.model.entities.Profile;

public class ProfileResponse {
    private Integer id;
    private String name;
    private String avatar;

    public ProfileResponse(Profile profile) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.avatar = profile.getAvatar();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
