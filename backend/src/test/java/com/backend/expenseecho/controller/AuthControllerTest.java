package com.backend.expenseecho.controller;

import com.backend.expenseecho.model.dto.AuthResponse;
import com.backend.expenseecho.model.dto.RegisterRequest;
import com.backend.expenseecho.model.entities.UserInfo;
import com.backend.expenseecho.security.JwtTokenProvider;
import com.backend.expenseecho.service.UserInfoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private UserInfoService mockUserInfoService;
    @Mock
    private JwtTokenProvider mockJwtTokenProvider;
    @Mock
    private AuthenticationManager mockAuthManager;
    @InjectMocks
    private AuthController sut;

    @Nested
    @DisplayName("register")
    class registerTests {
        private RegisterRequest mockRegisterRequest;
        private String mockAccessToken;

        @BeforeEach
        public void setUp() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
            mockRegisterRequest = new RegisterRequest("John", "Doe", "email@mail.com", "secretPassword");
            mockAccessToken = "mockAccessToken";
        }

        @Test
        public void register_returnsAuthResponse() throws Exception {
            UserInfo mockCreatedUser = new UserInfo(mockRegisterRequest.getFirstName(), mockRegisterRequest.getLastName(), mockRegisterRequest.getEmail(), mockRegisterRequest.getPassword());
            mockCreatedUser.setId(1);

            when(mockUserInfoService.register(any(UserInfo.class))).thenReturn(mockCreatedUser);
            when(mockJwtTokenProvider.generateToken(anyString(), anyString())).thenReturn(mockAccessToken);

            ResponseEntity<AuthResponse> response = sut.register(mockRegisterRequest);

            assertEquals(response.getStatusCode(), HttpStatus.CREATED);
            assertEquals(Objects.requireNonNull(response.getBody()).getAccessToken(), mockAccessToken);
        }
    }
}
