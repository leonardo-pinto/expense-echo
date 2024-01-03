package com.backend.expenseecho.utils.mapper;

import com.backend.expenseecho.model.dto.Budget.CreateBudgetRequest;
import com.backend.expenseecho.model.entities.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);
    Budget convert(CreateBudgetRequest createBudgetRequest);
}
