package com.example.ViChat.service;

import com.example.ViChat.dto.request.RoleRequest;
import com.example.ViChat.dto.response.RoleResponse;
import com.example.ViChat.entity.Role;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    Role findRoleById(String roleName);
    List<RoleResponse> getAllRole();
    void deleteRole(String roleName);
}
