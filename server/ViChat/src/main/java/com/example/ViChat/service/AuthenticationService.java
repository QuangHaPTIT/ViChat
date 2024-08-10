package com.example.ViChat.service;

import com.example.ViChat.dto.request.*;
import com.example.ViChat.dto.response.AuthResponse;
import com.example.ViChat.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthResponse createUser(UserCreationRequest userCreationRequest);
    AuthResponse authenticate(AuthRequest authRequest);
    IntrospectResponse introspect(IntrospectRequest introspectRequest);
    AuthResponse outboundAuthenticate(String code);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;
    void forgotPassword(String email);
    void verifyUserForgotPassword(ForgotPasswordRequest forgotPasswordRequest);
}
