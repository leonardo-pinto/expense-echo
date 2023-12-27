package com.backend.expenseecho.controller;

import com.backend.expenseecho.model.dto.CreateProfileRequest;
import com.backend.expenseecho.model.dto.ProfileResponse;
import com.backend.expenseecho.model.dto.UpdateProfileRequest;
import com.backend.expenseecho.model.entities.Profile;
import com.backend.expenseecho.service.ProfileService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {
    @InjectMocks
    private ProfileController sut;
    @Mock
    private ProfileService mockProfileService;

    @Mock
    private SecurityContext mockSecurityContext;
    @Mock
    private Authentication mockAuthentication;


    @BeforeEach
    public void setUp() {
      when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
      when(mockAuthentication.getName()).thenReturn("1");
      SecurityContextHolder.setContext(mockSecurityContext);
    }


    @Nested
    @DisplayName("getAllByUserId")
    class getAllByUserIdTests {

        List<ProfileResponse> mockProfileResponse = new ArrayList<>();
        @BeforeEach
        public void setUp() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            Profile mockProfile1 = new Profile();
            mockProfile1.setName("John");
            Profile mockProfile2 = new Profile();
            mockProfile2.setName("Jane");

            mockProfileResponse.add(new ProfileResponse(mockProfile1));
            mockProfileResponse.add(new ProfileResponse(mockProfile2));
        }

        @Test
        public void getAllByUserId_returnsProfileResponseList() {
            when(mockProfileService.getAllByUserId(anyString()))
                    .thenReturn(mockProfileResponse);

            ResponseEntity<List<ProfileResponse>> result = sut.getAllByUserId();

            assertEquals(result.getStatusCode(), HttpStatus.OK);
            assertEquals(Objects.requireNonNull(result.getBody()).size(), mockProfileResponse.size());
        }
    }

    @Nested
    @DisplayName("createProfile")
    class createProfileTests {
        private CreateProfileRequest mockCreateProfileRequest;
        private ProfileResponse mockProfileResponse;
        @BeforeEach
        public void setUp() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
            mockCreateProfileRequest = new CreateProfileRequest("John DD", "");
            Profile mockProfile = new Profile();
            mockProfile.setId(5);
            mockProfile.setName("John DD");
            mockProfileResponse = new ProfileResponse(mockProfile);
        }

        @Test
        public void createProfile_returnsProfileResponse() {
            when(mockProfileService.create(any(CreateProfileRequest.class), anyString()))
                    .thenReturn(mockProfileResponse);

            ResponseEntity<ProfileResponse> result = sut.createProfile(mockCreateProfileRequest);

            assertEquals(result.getStatusCode(), HttpStatus.CREATED);
            assertEquals(Objects.requireNonNull(result.getBody()), mockProfileResponse);
        }
    }

    @Nested
    @DisplayName("updateProfile")
    class updateProfileTests {
        private UpdateProfileRequest mockUpdateProfileRequest;
        private ProfileResponse mockProfileResponse;
        @BeforeEach
        public void setUp() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
            mockUpdateProfileRequest = new UpdateProfileRequest("John D.D.", "");
            Profile mockProfile = new Profile();
            mockProfile.setId(5);
            mockProfile.setName("John D.D.");
            mockProfileResponse = new ProfileResponse(mockProfile);
        }
        @Test
        public void updateProfile_returnsProfileResponse() {
            when(mockProfileService.updateProfile(anyString(), any(UpdateProfileRequest.class), anyString()))
                    .thenReturn(mockProfileResponse);

            ResponseEntity<ProfileResponse> result = sut.updateProfile("1", mockUpdateProfileRequest);

            assertEquals(result.getStatusCode(), HttpStatus.OK);
            assertEquals(Objects.requireNonNull(result.getBody()), mockProfileResponse);
        }
    }
}
