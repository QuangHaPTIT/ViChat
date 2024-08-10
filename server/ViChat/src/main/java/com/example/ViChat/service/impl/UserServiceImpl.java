package com.example.ViChat.service.impl;

import com.example.ViChat.component.JwtTokenUtil;
import com.example.ViChat.dto.request.UserUpdateRequest;
import com.example.ViChat.dto.response.UserResponse;
import com.example.ViChat.entity.User;
import com.example.ViChat.exception.AppException;
import com.example.ViChat.exception.ErrorCode;
import com.example.ViChat.mapper.UserMapper;
import com.example.ViChat.repository.UserRepository;
import com.example.ViChat.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User findUserById(String id) {
        User user = findByUserId(id);
        return user;
    }

    @Override
    public User findUserProfile() {
        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        String email =securityContextHolder.getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXIST));
        return user;
    }

    @Transactional
    @Override
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) throws ParseException, JOSEException {
        User user = findUserProfile();


        if(!user.getEmail().equals(userUpdateRequest.getEmail()) && (userRepository.existsByEmail(userUpdateRequest.getEmail()))) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if(userUpdateRequest.getPassword() != null && !passwordEncoder.matches(userUpdateRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.ORIGINAL_PASSWORD_INVALID);
        }
        if(userUpdateRequest.getNewPassword() != null && !userUpdateRequest.getNewPassword().equals(userUpdateRequest.getRetypeNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISS_MATCH);
        }
        userMapper.updateUser(user, userUpdateRequest);
        if(userUpdateRequest.getNewPassword() != null)
            user.setPassword(passwordEncoder.encode(userUpdateRequest.getNewPassword()));
        log.info(user.toString());
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> searchUser(String query) {
        List<UserResponse> userResponses = userRepository.searchUser(query)
                .stream().map(user -> userMapper.toUserResponse(user)).collect(Collectors.toList());
        return userResponses;
    }

    @Override
    @Transactional
    public void blockOrEnableUser(String userId, boolean isActive) {
        User user = findByUserId(userId);
        user.setIsActive(isActive);
        userRepository.save(user);
    }

    private User findByUserId(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        return user;
    }
}
