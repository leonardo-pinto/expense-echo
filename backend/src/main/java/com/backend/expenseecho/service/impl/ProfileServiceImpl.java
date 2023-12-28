package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
import com.backend.expenseecho.exception.ResourceNotFoundException;
import com.backend.expenseecho.exception.UnauthorizedException;
import com.backend.expenseecho.model.dto.CreateProfileRequest;
import com.backend.expenseecho.model.dto.ProfileResponse;
import com.backend.expenseecho.model.dto.UpdateProfileRequest;
import com.backend.expenseecho.model.entities.Profile;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.repository.ProfileRepository;
import com.backend.expenseecho.repository.UserRepository;
import com.backend.expenseecho.service.ProfileService;
import com.backend.expenseecho.utils.mapper.ProfileMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private ProfileRepository profileRepository;
    private UserRepository userRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProfileResponse create(CreateProfileRequest request, String userId) {
        User user = userRepository
                .findById(Integer.parseInt(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User account not found."));

        Profile profile = ProfileMapper.INSTANCE.convert(request);
        profile.setUser(user);
        if (profileNameAlreadyExists(request.getName(), profile.getUser().getId())) {
            throw new BadRequestException("Profile name already exists.");
        }
        Profile createdProfile = profileRepository.save(profile);
        return new ProfileResponse(createdProfile);
    }

    @Override
    public List<ProfileResponse> getAllByUserId(String userId) {
        List<Profile> profiles = profileRepository.findByUserId(Integer.parseInt(userId));
        return profiles.stream().map(ProfileResponse::new).toList();
    }

    @Override
    public ProfileResponse updateProfile(String id, UpdateProfileRequest request, String userId) {
        Profile profile = profileRepository
                .findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found."));

        if (!profile.getUser().getId().toString().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to perform this action.");
        }
        if (profileNameAlreadyExists(request.getName(), profile.getUser().getId())) {
            throw new BadRequestException("Profile name already exists.");
        }

        profile.setName(request.getName());
        profile.setAvatar(request.getAvatar());

        Profile updatedProfile = profileRepository.save(profile);
        return new ProfileResponse(updatedProfile);
    }

    public Boolean profileNameAlreadyExists(String profileName, int userId) {
        return profileRepository.findByNameAndUserId(profileName, userId).isPresent();
    }
}
