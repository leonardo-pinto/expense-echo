package com.backend.expenseecho.controller;

import com.backend.expenseecho.model.dto.Category.CategoryResponse;
import com.backend.expenseecho.model.dto.Category.CreateCategoryRequest;
import com.backend.expenseecho.model.dto.Category.UpdateCategoryRequest;
import com.backend.expenseecho.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> getAllByUserIdOrDefault() {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        List<CategoryResponse> response = categoryService.getAllByUserIdOrDefault(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        CategoryResponse response = categoryService.create(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable String id, @Valid @RequestBody UpdateCategoryRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        CategoryResponse response = categoryService.update(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
