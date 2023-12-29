package com.backend.expenseecho.service;

import com.backend.expenseecho.model.dto.CategoryResponse;
import com.backend.expenseecho.model.dto.CreateCategoryRequest;
import com.backend.expenseecho.model.dto.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CreateCategoryRequest request, String userId);
    List<CategoryResponse> getAllByUserIdOrDefault(String userId);
    CategoryResponse update(String id, UpdateCategoryRequest request, String userId);
}
