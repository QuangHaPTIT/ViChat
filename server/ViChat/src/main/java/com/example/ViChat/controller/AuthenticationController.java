package com.example.ViChat.controller;

import com.example.ViChat.dto.request.AuthRequest;
import com.example.ViChat.dto.request.ForgotPasswordRequest;
import com.example.ViChat.dto.request.RefreshTokenRequest;
import com.example.ViChat.dto.request.UserCreationRequest;
import com.example.ViChat.dto.response.ApiResponse;
import com.example.ViChat.dto.response.AuthResponse;
import com.example.ViChat.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> createUserHandle(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        AuthResponse authResponse = authenticationService.createUser(userCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .message("Create user success")
                        .result(authResponse)
                        .build());
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> loginHandle(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authenticationService.authenticate(authRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .message("Login success")
                        .result(authResponse)
                        .build());
    }

    @PostMapping("/outbound/authentication")
    public ResponseEntity<ApiResponse> outboundAuthenticate(@RequestParam("code") String code) {
        log.info("outbound", code);
        AuthResponse authResponse = authenticationService.outboundAuthenticate(code);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .message("Login with google success")
                        .result(authResponse)
                        .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        AuthResponse authResponse = authenticationService.refreshToken(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
               ApiResponse.builder()
                       .message("Refresh token success")
                       .result(authResponse)
                       .build()
        );
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam("email") String email) {
        authenticationService.forgotPassword(email);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("OTP has been send to your email")
                        .build());
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyCode(@RequestBody @Valid ForgotPasswordRequest passwordRequest) {
        authenticationService.verifyUserForgotPassword(passwordRequest);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("New password has been send to email successfully")
                        .build());
    }
}
