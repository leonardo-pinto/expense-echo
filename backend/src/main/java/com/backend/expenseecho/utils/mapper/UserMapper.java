package com.backend.expenseecho.utils.mapper;

import com.backend.expenseecho.model.dto.RegisterRequest;
import com.backend.expenseecho.model.dto.RegisterUserResponse;
import com.backend.expenseecho.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User convert(RegisterRequest registerRequest);
    RegisterUserResponse convert(User user);
}
