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
import com.backend.expenseecho.service.CategoryService;
import com.backend.expenseecho.utils.mapper.CategoryMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CategoryResponse create(CreateCategoryRequest request, String userId) {
        if(categoryNameAlreadyExists(request.getName(), Integer.parseInt(userId))) {
            throw new BadRequestException("Category name already exists.");
        }

        User user = userRepository
                .findById(Integer.parseInt(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User account not found."));

        Category category = CategoryMapper.INSTANCE.convert(request);
        category.setUser(user);
        Category createdCategory = categoryRepository.save(category);

        return CategoryMapper.INSTANCE.convert(createdCategory);
    }

    @Override
    public List<CategoryResponse> getAllByUserIdOrDefault(String userId) {
        List<Category> categories = categoryRepository.findByUserIdOrIsDefaultTrue(Integer.parseInt(userId));
        return categories.stream().map(CategoryMapper.INSTANCE::convert).toList();
    }

    @Override
    public CategoryResponse update(String id, UpdateCategoryRequest request, String userId) {
        Category category = categoryRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        if (category.getDefault()) {
            throw new BadRequestException("It is not possible to update a default category.");
        }
        if (categoryNameAlreadyExists(request.getName(), Integer.parseInt(userId))) {
            throw new BadRequestException("Category name already exists.");
        }
        if (!category.getUser().getId().toString().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to perform this action.");
        }

        category.setName(request.getName());
        categoryRepository.save(category);
        return CategoryMapper.INSTANCE.convert(category);
    }

    public Boolean categoryNameAlreadyExists(String categoryName, int userId) {
        return categoryRepository.findByNameAndUserIdOrIsDefaultTrue(categoryName, userId).isPresent();
    }
}
