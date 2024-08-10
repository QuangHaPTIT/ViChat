package com.example.ViChat.mapper;

import com.example.ViChat.dto.request.RoleRequest;
import com.example.ViChat.dto.response.RoleResponse;
import com.example.ViChat.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
    Role toRole(RoleRequest roleRequest);
}
