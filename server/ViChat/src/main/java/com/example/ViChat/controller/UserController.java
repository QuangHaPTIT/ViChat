package com.example.ViChat.controller;

import com.example.ViChat.dto.request.UserUpdateRequest;
import com.example.ViChat.dto.response.ApiResponse;
import com.example.ViChat.dto.response.UserResponse;
import com.example.ViChat.entity.User;
import com.example.ViChat.mapper.UserMapper;
import com.example.ViChat.service.UserService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@Tag(name = "User Controller")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get profile", description = "Get user profile")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserProfileHandle() {
        UserResponse userResponse = userMapper.toUserResponse(userService.findUserProfile());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.builder()
                .message("Get user profile success")
                .result(userResponse)
                .build());
    }

    @Operation(summary = "Get user", description = "Get user profile by id")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("userId") String userId) {
        UserResponse userResponse = userMapper.toUserResponse(userService.findUserById(userId));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.builder()
                .message("Get user detail success")
                .result(userResponse)
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchUserHandle(@RequestParam("name") String query) {
        List<UserResponse> userResponses = userService.searchUser(query);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Search user success")
                .result(userResponses)
                .build());
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandle(
                            @RequestBody @Valid UserUpdateRequest userUpdateRequest
                                            ) throws ParseException, JOSEException {
        UserResponse userResponse = userService.updateUser( userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Update user success")
                .result(userResponse)
                .build());
    }

    @DeleteMapping("/blockOrEnable/{userId}/{active}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> blockOrEnable(@PathVariable("userId") String userId,
                                                     @PathVariable("active") Boolean active) {
        userService.blockOrEnableUser(userId, active);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("Block user with id "+ userId + "successfully")
                        .build()
        );
    }

}
