package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
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
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl sut;
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
        private User mockUser;

        @BeforeEach
        public void setUp() {
            mockUser = new User("John", "Doe", "email@mail.com", "password");
        }

        @Test
        void register_whenEmailDoesNotExist_returnsUser(){
            User mockCreatedUser = new User("John", "Doe", "email@mail.com", "password");
            mockCreatedUser.setId(1);

            when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(mockPasswordEncoder.encode(anyString())).thenReturn("hashedPassword");
            when(mockUserRepository.save(any(User.class))).thenReturn(mockCreatedUser);

            User result = sut.register(mockUser);
            assertEquals(result.getId(), mockCreatedUser.getId());
            assertEquals(result.getEmail(), mockCreatedUser.getEmail());
        }

        @Test
        void register_whenEmailExist_throwsException(){
            when(mockUserRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

            Exception exception = assertThrows(BadRequestException.class, () -> sut.register(mockUser));
            assertEquals("Email already registered.", exception.getMessage());
        }
    }
}
