package com.backend.expenseecho.service;

import com.backend.expenseecho.model.dto.Category.CategoryResponse;
import com.backend.expenseecho.model.dto.Category.CreateCategoryRequest;
import com.backend.expenseecho.model.dto.Category.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest request, String userId);
    List<CategoryResponse> getAllByUserIdOrDefault(String userId);
    CategoryResponse update(String id, UpdateCategoryRequest request, String userId);
}
