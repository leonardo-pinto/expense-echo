package com.backend.expenseecho.controller;

import com.backend.expenseecho.model.dto.Auth.AuthResponse;
import com.backend.expenseecho.model.dto.Auth.LoginRequest;
import com.backend.expenseecho.model.dto.Auth.RegisterRequest;
import com.backend.expenseecho.model.dto.Auth.RegisterUserResponse;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.security.JwtTokenProvider;
import com.backend.expenseecho.security.UserInfoUserDetails;
import com.backend.expenseecho.service.AuthService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthService mockAuthService;
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
            RegisterUserResponse mockRegisterUserResponse = new RegisterUserResponse(1, mockRegisterRequest.getEmail());

            when(mockAuthService.register(any(RegisterRequest.class))).thenReturn(mockRegisterUserResponse);
            when(mockJwtTokenProvider.generateToken(anyString(), anyString())).thenReturn(mockAccessToken);

            ResponseEntity<AuthResponse> response = sut.register(mockRegisterRequest);

            assertEquals(response.getStatusCode(), HttpStatus.CREATED);
            assertEquals(Objects.requireNonNull(response.getBody()).getAccessToken(), mockAccessToken);
        }
    }

    @Nested
    @DisplayName("login")
    class loginTests {
        private LoginRequest mockLoginRequest;
        private String mockAccessToken;
        private UserInfoUserDetails mockUserInfoUserDetails;
        private Authentication mockAuthentication;

        @BeforeEach
        public void setUp() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
            mockLoginRequest = new LoginRequest("email@mail.com", "secretPassword");
            mockAccessToken = "mockAccessToken";
            mockAuthentication = mock(Authentication.class);
            User mockUser = new User("John", "Doe", "email@mail.com", "password");
            mockUser.setId(1);
            mockUserInfoUserDetails = new UserInfoUserDetails(mockUser);
        }

        @Test
        public void login_whenCredentialsAreValid_returnsAuthResponse() {
            when(mockAuthManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(mockAuthentication);
            when(mockAuthentication.getPrincipal()).thenReturn(mockUserInfoUserDetails);
            when(mockJwtTokenProvider.generateToken(anyString(), anyString())).thenReturn(mockAccessToken);
            ResponseEntity<AuthResponse> response = sut.login(mockLoginRequest);

            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(Objects.requireNonNull(response.getBody()).getAccessToken(), mockAccessToken);
        }
    }
}
