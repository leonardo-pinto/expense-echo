package com.backend.expenseecho.utils.mapper;

import com.backend.expenseecho.model.dto.Category.CategoryResponse;
import com.backend.expenseecho.model.dto.Category.CreateCategoryRequest;
import com.backend.expenseecho.model.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category convert(CreateCategoryRequest createCategoryRequest);
    CategoryResponse convert(Category category);
}
