package com.example.ViChat.controller;

import com.example.ViChat.dto.request.RoleRequest;
import com.example.ViChat.dto.response.ApiResponse;
import com.example.ViChat.dto.response.RoleResponse;
import com.example.ViChat.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/roles")
@Tag(name = "Role Controller")
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRoleHandle(@RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.createRole(roleRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Create role success")
                .result(roleResponse)
                .build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{roleName}")
    public ResponseEntity<ApiResponse> deleteRoleHandle(@PathVariable("roleName") String roleName) {
        roleService.deleteRole(roleName);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Delete role success")
                .build());
    }


}
