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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    @InjectMocks
    private ProfileServiceImpl sut;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ProfileRepository mockProfileRepository;

    @Nested
    @DisplayName("create")
    class createTests {

        private User mockUser;
        private Profile mockProfile;
        private CreateProfileRequest mockCreateProfileRequest;

        @BeforeEach
        public void setUp() {
            mockCreateProfileRequest = new CreateProfileRequest("John DD", "");
            mockUser = new User("John", "Doe", "email@mail.com", "password");
            mockUser.setId(1);
            mockProfile = new Profile("John DD", "", mockUser, 1);
        }

        @Test
        void create_whenSuccessful_returnsProfileResponse() {
            when(mockUserRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockUser));
            when(mockProfileRepository.findByNameAndUserId(anyString(), anyInt())).thenReturn(Optional.empty());

            when(mockProfileRepository.save(any(Profile.class)))
                    .thenReturn(mockProfile);

            ProfileResponse response = sut.create(mockCreateProfileRequest, "1");

            assertEquals(response.getId(), mockProfile.getId());
            assertEquals(response.getName(), mockProfile.getName());
            assertEquals(response.getAvatar(), mockProfile.getAvatar());
        }

        @Test
        void create_whenUserNotFound_throwsException() {
            when(mockUserRepository.findById(anyInt()))
                    .thenReturn(Optional.empty());

            Exception exception = assertThrows(UsernameNotFoundException.class, () -> sut.create(mockCreateProfileRequest, "1"));

            assertEquals("User account not found.", exception.getMessage());
        }

        @Test
        void create_whenProfileNameAlreadyExists_throwsException() {
            when(mockUserRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockUser));
            when(mockProfileRepository.findByNameAndUserId(anyString(), anyInt())).thenReturn(Optional.of(mockProfile));

            Exception exception = assertThrows(BadRequestException.class, () -> sut.create(mockCreateProfileRequest, "1"));

            assertEquals("Profile name already exists.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("updateProfile")
    class updateProfileTests {

        private User mockUser;
        private Profile mockProfile;
        private UpdateProfileRequest mockUpdateProfileRequest;

        @BeforeEach
        public void setUp() {
            mockUpdateProfileRequest = new UpdateProfileRequest("Johny D", "something");
            mockUser = new User("John", "Doe", "email@mail.com", "password");
            mockUser.setId(1);
            mockProfile = new Profile("John DD", "", mockUser, 1);
        }

        @Test
        void update_whenSuccessful_returnsProfileResponse() {
            when(mockProfileRepository.findById(anyInt())).thenReturn(Optional.of(mockProfile));

            when(mockProfileRepository.findByNameAndUserId(anyString(), anyInt())).thenReturn(Optional.empty());

            Profile mockUpdatedProfile = new Profile(mockUpdateProfileRequest.getName(), mockUpdateProfileRequest.getAvatar(), mockUser, mockProfile.getId());

            when(mockProfileRepository.save(any(Profile.class)))
                    .thenReturn(mockUpdatedProfile);

            ProfileResponse result = sut.updateProfile("1", mockUpdateProfileRequest, "1");

            assertEquals(result.getName(), mockUpdateProfileRequest.getName());
            assertEquals(result.getAvatar(), mockUpdateProfileRequest.getAvatar());
            assertEquals(result.getId(), mockProfile.getId());
        }

        @Test
        void update_whenProfileNotFound_throwsException() {
            when(mockProfileRepository.findById(anyInt())).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> sut.updateProfile("1", mockUpdateProfileRequest, "1"));

            assertEquals("Profile not found.", exception.getMessage());
        }

        @Test
        void update_whenUserIdDoesNotMatch_throwsException() {
            mockProfile.setId(6);
            when(mockProfileRepository.findById(anyInt())).thenReturn(Optional.of(mockProfile));

            Exception exception = assertThrows(UnauthorizedException.class, () -> sut.updateProfile("1", mockUpdateProfileRequest, "6"));

            assertEquals("User is not authorized to perform this action.", exception.getMessage());
        }

        @Test
        void update_whenProfileNameAlreadyExists_throwsException() {
            when(mockProfileRepository.findById(anyInt())).thenReturn(Optional.of(mockProfile));

            when(mockProfileRepository.findByNameAndUserId(anyString(), anyInt())).thenReturn(Optional.of(mockProfile));

            Exception exception = assertThrows(BadRequestException.class, () -> sut.updateProfile("1", mockUpdateProfileRequest ,"1"));

            assertEquals("Profile name already exists.", exception.getMessage());
        }
    }
}
