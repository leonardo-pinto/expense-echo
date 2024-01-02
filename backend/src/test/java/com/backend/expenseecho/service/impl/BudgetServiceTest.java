package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
import com.backend.expenseecho.exception.ResourceNotFoundException;
import com.backend.expenseecho.exception.UnauthorizedException;
import com.backend.expenseecho.model.dto.Budget.BudgetResponse;
import com.backend.expenseecho.model.dto.Budget.CreateBudgetRequest;
import com.backend.expenseecho.model.dto.Budget.UpdateBudgetRequest;
import com.backend.expenseecho.model.entities.Budget;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.repository.BudgetRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {
    @InjectMocks
    private BudgetServiceImpl sut;
    @Mock
    private BudgetRepository mockBudgetRepository;
    @Mock
    private UserRepository mockUserRepository;

    @Nested
    @DisplayName("create")
    class createTests {

        private CreateBudgetRequest mockCreateBudgetRequest;
        private User mockUser;

        @BeforeEach
        public void setUp() {
            mockCreateBudgetRequest = new CreateBudgetRequest("Budget for testing");
            mockUser = new User("John", "Doe", "mock@mail.com", "123456");
            mockUser.setId(1);
            when(mockUserRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockUser));
        }

        @Test
        void create_whenSuccessful_returnsBudgetResponse() {
            when(mockBudgetRepository.findByNameAndUserId(anyString(), anyInt()))
                    .thenReturn(Optional.empty());
            Budget mockCreatedBudget = new Budget(1, mockCreateBudgetRequest.getName(), mockUser);
            when(mockBudgetRepository.save(any(Budget.class)))
                    .thenReturn(mockCreatedBudget);

            BudgetResponse result = sut.create(mockCreateBudgetRequest, "1");

            assertEquals(result.getName(), mockCreateBudgetRequest.getName());
            assertEquals(result.getId(), mockCreatedBudget.getId());
        }

        @Test
        void create_whenUserNotFound_throwsException() {
            when(mockUserRepository.findById(anyInt()))
                    .thenReturn(Optional.empty());

            Exception exception = assertThrows(UsernameNotFoundException.class, () -> sut.create(mockCreateBudgetRequest, "1"));
            assertEquals("User account not found.", exception.getMessage());
        }

        @Test
        void create_whenBudgetNameAlreadyExist_throwsException() {
            Budget mockBudget = new Budget();
            when(mockBudgetRepository.findByNameAndUserId(anyString(), anyInt()))
                    .thenReturn(Optional.of(mockBudget));

            Exception exception = assertThrows(BadRequestException.class, () -> sut.create(mockCreateBudgetRequest, "1"));
            assertEquals("Budget name already exists.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("getAllByUserId")
    class getAllByUserIdTests{
        List<Budget> mockBudgets = new ArrayList<>();

        @BeforeEach
        public void setUp() {
            Budget mockBudget1 = new Budget();
            Budget mockBudget2 = new Budget();
            Budget mockBudget3 = new Budget();
            mockBudgets.addAll(
                    Arrays.asList(mockBudget1, mockBudget2, mockBudget3)
            );
            when(mockBudgetRepository.findByUserId(anyInt()))
                    .thenReturn(mockBudgets);
        }

        @Test
        void getAllByUserId_whenSuccessful_returnsBudgetResponseList() {
            List<BudgetResponse> result = sut.getAllByUserId("1");
            assertEquals(result.size(), mockBudgets.size());
        }

        @Test
        void getAllByUserId_whenThereAreNotBudgets_returnsEmptyArray() {
            mockBudgets.clear();
            when(mockBudgetRepository.findByUserId(anyInt()))
                    .thenReturn(mockBudgets);
            List<BudgetResponse> result = sut.getAllByUserId("1");

            assertEquals(result.size(), 0);
        }
    }

    @Nested
    @DisplayName("update")
    class updateTests {

        private UpdateBudgetRequest mockUpdateBudgetRequest;
        private Budget mockBudget;
        @BeforeEach
        public void setUp() {
            mockUpdateBudgetRequest = new UpdateBudgetRequest("Budget for testing");
            User mockUser = new User("John", "Doe", "mock@mail.com", "123456");
            mockUser.setId(1);
            mockBudget =  new Budget(1, "updated budget", mockUser);
            when(mockBudgetRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockBudget));
        }
        @Test
        void update_whenSuccessful_returnsBudgetResponse() {
            when(mockBudgetRepository.findByNameAndUserId(anyString(), anyInt()))
                    .thenReturn(Optional.empty());
            Budget mockCreatedBudget = new Budget(mockBudget.getId(), mockUpdateBudgetRequest.getName(), mockBudget.getUser());
            when(mockBudgetRepository.save(any(Budget.class))).thenReturn(mockCreatedBudget);
            BudgetResponse result = sut.update("1", mockUpdateBudgetRequest, "1");

            assertEquals(result.getId(), mockBudget.getId());
            assertEquals(result.getName(), mockUpdateBudgetRequest.getName());
        }

        @Test
        void update_whenBudgetNotFound_throwsException() {
            when(mockBudgetRepository.findById(anyInt()))
                    .thenReturn(Optional.empty());
            Exception exception = assertThrows(ResourceNotFoundException.class, () -> sut.update("1", mockUpdateBudgetRequest, "1"));
            assertEquals("Budget not found.", exception.getMessage());
        }

        @Test
        void update_whenBudgetNameAlreadyExists_throwsException() {
            when(mockBudgetRepository.findByNameAndUserId(anyString(), anyInt()))
                    .thenReturn(Optional.of(mockBudget));

            Exception exception = assertThrows(BadRequestException.class, () -> sut.update("1", mockUpdateBudgetRequest, "1"));
            assertEquals("Budget name already exists.", exception.getMessage());
        }

        @Test
        void update_whenUserIdDoesNotMatch_throwsException() {
            when(mockBudgetRepository.findByNameAndUserId(anyString(), anyInt()))
                    .thenReturn(Optional.empty());
            Exception exception = assertThrows(UnauthorizedException.class, () -> sut.update("1", mockUpdateBudgetRequest, "6"));
            assertEquals("User is not authorized to perform this action.", exception.getMessage());
        }
    }
}
