package com.backend.expenseecho.utils.mapper;

import com.backend.expenseecho.model.dto.CategoryResponse;
import com.backend.expenseecho.model.dto.CreateCategoryRequest;
import com.backend.expenseecho.model.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    Category convert(CreateCategoryRequest createCategoryRequest);
    CategoryResponse convert(Category category);
}
