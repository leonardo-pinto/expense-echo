package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
import com.backend.expenseecho.model.entities.UserInfo;
import com.backend.expenseecho.repository.UserInfoRepository;
import com.backend.expenseecho.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;


public class UserInfoServiceTest {

    private UserInfoService sut;
    private UserInfoRepository mockUserInfoRepository;
    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    public void setUp() {
        mockUserInfoRepository = mock(UserInfoRepository.class);
        mockPasswordEncoder = mock(PasswordEncoder.class);
        sut = new UserInfoServiceImpl(mockUserInfoRepository, mockPasswordEncoder);
    }

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
            UserInfo user = new UserInfo("John", "Doe", mockEmail, "password");
            when(mockUserInfoRepository.findByEmail(mockEmail)).thenReturn(Optional.of(user));

            boolean result = sut.emailAlreadyExists(mockEmail);
            assertTrue(result);
        }

        @Test
        void emailAlreadyExists_whenEmailDoesNotExist_returnsTrue(){
            when(mockUserInfoRepository.findByEmail(mockEmail)).thenReturn(Optional.empty());
            boolean result = sut.emailAlreadyExists(mockEmail);
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("register")
    class registerTests {
        private UserInfo mockUser;

        @BeforeEach
        public void setUp() {
            mockUser = new UserInfo("John", "Doe", "email@mail.com", "password");
        }

        @Test
        void register_whenEmailDoesNotExist_returnsUser(){
            UserInfo mockCreatedUser = new UserInfo("John", "Doe", "email@mail.com", "password");
            mockCreatedUser.setId(1);

            when(mockUserInfoRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(mockPasswordEncoder.encode(anyString())).thenReturn("hashedPassword");
            when(mockUserInfoRepository.save(any(UserInfo.class))).thenReturn(mockCreatedUser);

            UserInfo result = sut.register(mockUser);
            assertEquals(result.getId(), mockCreatedUser.getId());
            assertEquals(result.getEmail(), mockCreatedUser.getEmail());
        }

        @Test
        void register_whenEmailExist_throwsException(){
            when(mockUserInfoRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

            Exception exception = assertThrows(BadRequestException.class, () -> sut.register(mockUser));
            assertEquals("Email already registered.", exception.getMessage());
        }

    }
}
