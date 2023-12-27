package com.backend.expenseecho.utils.mapper;

import com.backend.expenseecho.model.dto.CreateProfileRequest;
import com.backend.expenseecho.model.entities.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);
    Profile convert(CreateProfileRequest createProfileRequest);
}
