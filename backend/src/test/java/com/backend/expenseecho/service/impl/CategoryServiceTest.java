package com.backend.expenseecho.service.impl;

import com.backend.expenseecho.exception.BadRequestException;
import com.backend.expenseecho.exception.ResourceNotFoundException;
import com.backend.expenseecho.exception.UnauthorizedException;
import com.backend.expenseecho.model.dto.CategoryResponse;
import com.backend.expenseecho.model.dto.CreateCategoryRequest;
import com.backend.expenseecho.model.dto.UpdateCategoryRequest;
import com.backend.expenseecho.model.entities.Category;
import com.backend.expenseecho.model.entities.User;
import com.backend.expenseecho.repository.CategoryRepository;
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
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl sut;
    @Mock
    private CategoryRepository mockCategoryRepository;
    @Mock
    private UserRepository mockUserRepository;

    @Nested
    @DisplayName("create")
    class createTests {
        private CreateCategoryRequest mockCreateCategoryRequest;
        private User mockUser;

        @BeforeEach
        public void setUp() {
            mockCreateCategoryRequest = new CreateCategoryRequest("mock");
            mockUser = new User("John", "Doe", "mock@mail.com", "123456");
            mockUser.setId(1);
            when(mockCategoryRepository.findByNameAndUserIdOrIsDefaultTrue(anyString(), anyInt()))
                    .thenReturn(Optional.empty());
        }

        @Test
        void create_whenSuccessful_returnsCategoryResponse() {
            when(mockUserRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockUser));
            Category mockCreatedCategory = new Category(mockCreateCategoryRequest.getName(), mockUser);
            mockCreatedCategory.setId(1);

            when(mockCategoryRepository.save(any(Category.class)))
                    .thenReturn(mockCreatedCategory);

            CategoryResponse result = sut.create(mockCreateCategoryRequest, mockUser.getId().toString());

            assertEquals(result.getName(), mockCreateCategoryRequest.getName());
            assertEquals(result.getDefault(), false);
        }

        @Test
        void create_whenCategoryNameAlreadyExists_throwsException() {
            Category mockCategory = new Category();
            when(mockCategoryRepository.findByNameAndUserIdOrIsDefaultTrue(anyString(), anyInt()))
                    .thenReturn(Optional.of(mockCategory));

            Exception exception = assertThrows(BadRequestException.class, () -> sut.create(mockCreateCategoryRequest, "1"));
            assertEquals("Category name already exists.", exception.getMessage());
        }

        @Test
        void create_whenUserIsNotFound_throwsException() {
            when(mockUserRepository.findById(anyInt()))
                    .thenReturn(Optional.empty());

            Exception exception = assertThrows(UsernameNotFoundException.class, () -> sut.create(mockCreateCategoryRequest, "1"));
            assertEquals("User account not found.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("getAllByUserIdOrDefault")
    class getAllByUserIdOrDefaultTests {

        List<Category> mockCategories = new ArrayList<>();

        @BeforeEach
        public void setUp() {
            Category mockCategory1 = new Category();
            Category mockCategory2 = new Category();
            Category mockCategory3 = new Category();
            mockCategories.addAll(Arrays.asList(mockCategory1, mockCategory2, mockCategory3));
        }

        @Test
        void getAllByUserIdOrDefault_whenSuccessful_returnsCategoryResponseList() {
            when(mockCategoryRepository.findByUserIdOrIsDefaultTrue(anyInt()))
                    .thenReturn(mockCategories);
            List<CategoryResponse> result = sut.getAllByUserIdOrDefault("1");
            assertEquals(result.size(), mockCategories.size());
        }

        @Test
        void getAllByUserIdOrDefault_whenSuccessful_returnsEmptyArray() {
            mockCategories.clear();
            when(mockCategoryRepository.findByUserIdOrIsDefaultTrue(anyInt()))
                    .thenReturn(mockCategories);
            List<CategoryResponse> result = sut.getAllByUserIdOrDefault("1");
            assertEquals(result.size(), 0);
        }
    }

    @Nested
    @DisplayName("update")
    class updateTests {
        private UpdateCategoryRequest mockUpdateCategoryRequest;
        private User mockUser;

        @BeforeEach
        public void setUp() {
            mockUser = new User("John", "Doe", "mock@mail.com", "123456");
            mockUser.setId(1);
            mockUpdateCategoryRequest = new UpdateCategoryRequest("updated category");
        }

        @Test
        void update_whenSuccessful_returnsCategoryResponse() {
            Category mockCategory = new Category("original name", mockUser);
            mockCategory.setId(1);

            when(mockCategoryRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockCategory));

            when(mockCategoryRepository.findByNameAndUserIdOrIsDefaultTrue(anyString(), anyInt()))
                    .thenReturn(Optional.empty());
            when(mockCategoryRepository.save(any(Category.class)))
                    .thenReturn(any(Category.class));

            CategoryResponse result = sut.update("1", mockUpdateCategoryRequest, "1");
            assertEquals(result.getName(), mockUpdateCategoryRequest.getName());
            assertEquals(result.getId(), mockCategory.getId());
        }

        @Test
        void update_whenCategoryNotFound_throwsException() {
            when(mockCategoryRepository.findById(anyInt()))
                    .thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> sut.update("1", mockUpdateCategoryRequest, "1"));
            assertEquals("Category not found.", exception.getMessage());
        }

        @Test
        void update_whenCategoryIsDefault_throwsException() {
            Category mockCategory = new Category("original name", mockUser);
            mockCategory.setId(1);
            mockCategory.setDefault(true);

            when(mockCategoryRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockCategory));

            when(mockCategoryRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockCategory));
            Exception exception = assertThrows(BadRequestException.class, () -> sut.update("1", mockUpdateCategoryRequest, "1"));
            assertEquals("It is not possible to update a default category.", exception.getMessage());
        }

        @Test
        void update_whenUserIdDoesNotMatch_throwsException() {
            Category mockCategory = new Category("original name", mockUser);
            mockCategory.setId(1);

            when(mockCategoryRepository.findById(anyInt()))
                    .thenReturn(Optional.of(mockCategory));

            when(mockCategoryRepository.findByNameAndUserIdOrIsDefaultTrue(anyString(), anyInt()))
                    .thenReturn(Optional.empty());

            Exception exception = assertThrows(UnauthorizedException.class, () -> sut.update("1", mockUpdateCategoryRequest, "5"));
            assertEquals("User is not authorized to perform this action.", exception.getMessage());
        }
    }
}
