package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
import com.backend.expenseecho.model.dto.RegisterRequest;
import com.backend.expenseecho.model.dto.RegisterUserResponse;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl sut;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Nested
    @DisplayName("emailAlreadyExists")
    class emailAlreadyExistsTests {
        private String mockEmail;

        @BeforeEach
        public void setUp() {
            mockEmail = "mockEmail";
        }

        @Test
        void emailAlreadyExists_whenEmailExist_returnsTrue(){
            User user = new User("John", "Doe", mockEmail, "password");
            when(mockUserRepository.findByEmail(mockEmail)).thenReturn(Optional.of(user));

            boolean result = sut.emailAlreadyExists(mockEmail);
            assertTrue(result);
        }

        @Test
        void emailAlreadyExists_whenEmailDoesNotExist_returnsTrue(){
            when(mockUserRepository.findByEmail(mockEmail)).thenReturn(Optional.empty());
            boolean result = sut.emailAlreadyExists(mockEmail);
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("register")
    class registerTests {
        private RegisterRequest mockRegisterRequest;
        private User mockUser;

        @BeforeEach
        public void setUp() {
            mockRegisterRequest = new RegisterRequest("John", "Doe", "email@mail.com", "password");
            mockUser = new User("John", "Doe", "email@mail.com", "password");
            mockUser.setId(1);
        }

        @Test
        void register_whenEmailDoesNotExist_returnsRegisterUserResponse(){
            when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(mockPasswordEncoder.encode(anyString())).thenReturn("hashedPassword");
            when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);

            RegisterUserResponse result = sut.register(mockRegisterRequest);
            assertEquals(result.getId(), mockUser.getId());
            assertEquals(result.getEmail(), mockUser.getEmail());
        }

        @Test
        void register_whenEmailExist_throwsException(){
            when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

            Exception exception = assertThrows(BadRequestException.class, () -> sut.register(mockRegisterRequest));
            assertEquals("Email already registered.", exception.getMessage());
        }
    }
}
