package com.backend.expenseecho.model.dto.Profile;

import jakarta.validation.constraints.NotBlank;

public class CreateProfileRequest {

    @NotBlank(message = "Profile name is required")
    private String name;

    private String avatar;

    public CreateProfileRequest(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
