package com.example.ViChat.service.impl;

import com.example.ViChat.dto.request.RoleRequest;
import com.example.ViChat.dto.response.RoleResponse;
import com.example.ViChat.entity.Role;
import com.example.ViChat.exception.AppException;
import com.example.ViChat.exception.ErrorCode;
import com.example.ViChat.mapper.RoleMapper;
import com.example.ViChat.repository.RoleRepository;
import com.example.ViChat.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        if(roleRepository.existsByName(roleRequest.getName())) throw new AppException(ErrorCode.ROLE_EXISTED);
        Role role = roleMapper.toRole(roleRequest);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public Role findRoleById(String roleName) {
        Role role = roleRepository.findById(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));
        return role;
    }

    @Override
    public List<RoleResponse> getAllRole() {
        List<RoleResponse> roleResponses = roleRepository.findAll()
                .stream().map(role -> roleMapper.toRoleResponse(role)).collect(Collectors.toList());

        return roleResponses;
    }

    @Override
    @Transactional
    public void deleteRole(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
