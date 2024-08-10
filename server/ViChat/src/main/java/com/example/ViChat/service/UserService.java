package com.example.ViChat.service;

import com.example.ViChat.dto.request.UserUpdateRequest;
import com.example.ViChat.dto.response.UserResponse;
import com.example.ViChat.entity.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.util.List;

public interface UserService {
    User findUserById(String id);
    User findUserProfile();
    UserResponse updateUser( UserUpdateRequest userUpdateRequest) throws ParseException, JOSEException;
    List<UserResponse> searchUser(String query);
    void blockOrEnableUser(String userId, boolean isActive);
}
