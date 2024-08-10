package com.example.ViChat.service;

import com.example.ViChat.entity.RefreshToken;
import com.example.ViChat.entity.User;

public interface RefreshTokenService {
    RefreshToken createOrUpdate(User user, String token);
    void deleteRefreshToken(String userId);
}
