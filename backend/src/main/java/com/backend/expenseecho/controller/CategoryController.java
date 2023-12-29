package com.backend.expenseecho.controller;

import com.backend.expenseecho.model.dto.CategoryResponse;
import com.backend.expenseecho.model.dto.CreateCategoryRequest;
import com.backend.expenseecho.model.dto.UpdateCategoryRequest;
import com.backend.expenseecho.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<CategoryResponse>> getAllByUserIdOrDefault() {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        List<CategoryResponse> response = categoryService.getAllByUserIdOrDefault(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        CategoryResponse response = categoryService.create(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable String id, @Valid @RequestBody UpdateCategoryRequest request) {
        String userId = (SecurityContextHolder.getContext().getAuthentication()).getName();
        CategoryResponse response = categoryService.update(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
