package com.example.ViChat.service.impl;

import com.example.ViChat.component.JwtTokenUtil;
import com.example.ViChat.entity.RefreshToken;
import com.example.ViChat.entity.User;
import com.example.ViChat.exception.AppException;
import com.example.ViChat.exception.ErrorCode;
import com.example.ViChat.repository.RefreshTokenRepository;
import com.example.ViChat.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @Override
    @Transactional
    public RefreshToken createOrUpdate(User user, String token) {
        return refreshTokenRepository.findById(user.getId())
                .map(existingRefreshToken -> {
                    existingRefreshToken.setRefreshToken(token);
                    existingRefreshToken.setExpiresAt(getExpirationTimeFromToken(token));
                    return refreshTokenRepository.save(existingRefreshToken);
                }).orElseGet(() -> {
                        RefreshToken newRefresh = RefreshToken.builder()
                                .user(user)
                                .refreshToken(token)
                                .expiresAt(getExpirationTimeFromToken(token))
                                .build();
                        return refreshTokenRepository.save(newRefresh);
                    }
                );
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String userId) {

    }

    private Date getExpirationTimeFromToken(String token) {
        try{
            return jwtTokenUtil.extractExpirationTimeFromToken(token);
        }catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
