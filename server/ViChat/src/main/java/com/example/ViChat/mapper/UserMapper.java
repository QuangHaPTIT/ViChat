package com.example.ViChat.mapper;

import com.example.ViChat.dto.request.UserCreationRequest;
import com.example.ViChat.dto.request.UserUpdateRequest;
import com.example.ViChat.dto.response.UserResponse;
import com.example.ViChat.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(target = "isActive", constant = "true")
    User toUser(UserCreationRequest userCreationRequest);
    UserResponse toUserResponse(User user);

    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
