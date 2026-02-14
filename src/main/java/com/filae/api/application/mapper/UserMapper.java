package com.filae.api.application.mapper;

import com.filae.api.application.dto.user.UserResponse;
import com.filae.api.domain.entity.User;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for User entity to UserResponse DTO
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}

