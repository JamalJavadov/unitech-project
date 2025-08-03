package com.example.unitechproject.model.mapper;

import com.example.unitechproject.model.dto.userDto.UserRequestDto;
import com.example.unitechproject.model.dto.userDto.UserResponseDto;
import com.example.unitechproject.model.dto.userDto.UserUpdateDto;
import com.example.unitechproject.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toEntity(UserRequestDto userRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userName", source = "fullName")
    UserResponseDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toDto(UserUpdateDto userUpdateDto, @MappingTarget User user);
}
