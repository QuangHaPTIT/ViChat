package com.example.ViChat.service.impl;

import com.example.ViChat.component.JwtTokenUtil;
import com.example.ViChat.constant.RoleStatus;
import com.example.ViChat.dto.request.*;
import com.example.ViChat.dto.response.AuthResponse;
import com.example.ViChat.dto.response.ExchangeTokenResponse;
import com.example.ViChat.dto.response.IntrospectResponse;
import com.example.ViChat.dto.response.OutboundUserResponse;
import com.example.ViChat.entity.OtpCode;
import com.example.ViChat.entity.Role;
import com.example.ViChat.entity.User;
import com.example.ViChat.exception.AppException;
import com.example.ViChat.exception.ErrorCode;
import com.example.ViChat.mapper.UserMapper;
import com.example.ViChat.repository.OtpCodeRepository;
import com.example.ViChat.repository.UserRepository;
import com.example.ViChat.repository.httpclient.OutboundClient;
import com.example.ViChat.repository.httpclient.OutboundUserClient;
import com.example.ViChat.service.AuthenticationService;
import com.example.ViChat.service.MailService;
import com.example.ViChat.service.RefreshTokenService;
import com.example.ViChat.service.RoleService;
import com.example.ViChat.utils.MailUtil;
import com.example.ViChat.utils.VerifyUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final OutboundClient outboundClient;
    private final OutboundUserClient outboundUserClient;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;
    private final OtpCodeRepository otpCodeRepository;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${outbound.vichat.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.vichat.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.vichat.redirect-uri}")
    protected String REDIRECT_URI;

    protected final String GRANT_TYPE = "authorization_code";


    @Override
    @Transactional
    public AuthResponse createUser(UserCreationRequest userCreationRequest) {
        if(userRepository.existsByEmail(userCreationRequest.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        if(!userCreationRequest.getPassword().equals(userCreationRequest.getRetypePassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISS_MATCH);
        }
        Role role = roleService.findRoleById(RoleStatus.USER_ROLE);
        Set<Role> roles =Set.of(role);
        User user = userMapper.toUser(userCreationRequest);
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        user.setRoles(roles);
        user = userRepository.save(user);
        AuthResponse authResponse = generateAccessTokenAndRefreshToken(user);
        refreshTokenService.createOrUpdate(user, authResponse.getRefreshToken());
        try{
            Map<String, Object> props = new HashMap<>();
            props.put("fullname", user.getFullName());
            props.put("email", user.getEmail());

            MailRequest mailRequest = MailRequest.builder()
                    .to(user.getEmail())
                    .subject(MailUtil.SEND_EMAIL_SUBJECT.CLIENT_REGISTER)
                    .props(props)
                    .build();
            mailService.sendHtmlMail(mailRequest, MailUtil.TEMPLATE_EMAIL_CRETE_USER.TEMPLATE_GMAIL_REGISTER);
        }catch (MessagingException e) {
            e.printStackTrace();
        }
        return authResponse;
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));
        boolean matchPassword = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if(!matchPassword) throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        if(!user.getIsActive()) throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        AuthResponse authResponse = generateAccessTokenAndRefreshToken(user);
        refreshTokenService.createOrUpdate(user, authResponse.getRefreshToken());
        return authResponse;
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        String token = introspectRequest.getToken();
        boolean isValid = true;
        try{
            jwtTokenUtil.verifyToken(token);
        }catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse outboundAuthenticate(String code) {
        ExchangeTokenRequest exchangeTokenRequest = ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .grantType(GRANT_TYPE)
                .redirectUri(REDIRECT_URI)
                .build();
        ExchangeTokenResponse response = outboundClient.exchangeToken(exchangeTokenRequest);
        log.info("ExchangeTokenResponse ", response);
        log.info("Access token ", response.getAccessToken());
        OutboundUserResponse outboundUserResponse = outboundUserClient.getUserInfo("json", response.getAccessToken());
        User user = userRepository.findByEmail(outboundUserResponse.getEmail())
                    .orElseGet(() -> userRepository.save(
                            User.builder()
                                    .email(outboundUserResponse.getEmail())
                                    .fullName(outboundUserResponse.getName())
                                    .profilePicture(outboundUserResponse.getPicture())
                                    .isActive(true)
                                    .roles(new HashSet<>(Set.of(
                                            Role.builder().name(RoleStatus.USER_ROLE).build())))
                                    .build()
                    ));
        if(!user.getIsActive()) throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        String token = jwtTokenUtil.generateToken(user, VALID_DURATION);
        String refreshToken = jwtTokenUtil.generateToken(user, REFRESHABLE_DURATION);
        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .isAuth(true)
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        var signToken = jwtTokenUtil.verifyToken(refreshTokenRequest.getToken()); // kiểm tra đúng token và còn hạn hay không
        String email = signToken.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXIST));
        String token = jwtTokenUtil.generateToken(user, VALID_DURATION);
        AuthResponse authResponse = AuthResponse.builder()
                                    .accessToken(token)
                                    .refreshToken(refreshTokenRequest.getToken())
                                    .build();
        return authResponse;
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        Optional<OtpCode> optOtp = otpCodeRepository.findById(user.getId());
        String verifyCode = VerifyUtil.generateVerificationCode();
        if(!optOtp.isPresent()) {
            OtpCode otpCode = OtpCode.builder()
                    .user(user)
                    .expiresAt(LocalDateTime.now().plusMinutes(5))
                    .verifyCode(verifyCode)
                    .build();
            otpCodeRepository.save(otpCode);
        }else{
            optOtp.get().setExpiresAt(LocalDateTime.now().plusMinutes(5));
            optOtp.get().setVerifyCode(verifyCode);
            otpCodeRepository.save(optOtp.get());
        }
        try{
            Map<String, Object> props = new HashMap<>();
            props.put("verificationCode", verifyCode);
            MailRequest mailRequest = MailRequest.builder()
                    .to(user.getEmail())
                    .subject(MailUtil.SEND_EMAIL_SUBJECT.CLIENT_OTP)
                    .props(props)
                    .build();
            mailService.sendHtmlMail(mailRequest, MailUtil.TEMPLATE_EMAIL_CRETE_USER.TEMPALTE_SEND_VERIFY_CODE);
        }catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void verifyUserForgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(()  -> new AppException(ErrorCode.USER_NOT_EXIST));
        OtpCode otpCode = otpCodeRepository.findById(user.getId()).orElseThrow(() -> new AppException(ErrorCode.VERIFY_CODE_INCORRECT));
        if(otpCode.getExpiresAt().isBefore(LocalDateTime.now())) throw new AppException(ErrorCode.VERIFY_CODE_HAS_EXPIRED);
        if(!forgotPasswordRequest.getVerifyCode().equals(otpCode.getVerifyCode())) throw new AppException(ErrorCode.VERIFY_CODE_INCORRECT);
        String newPassword = VerifyUtil.generatePassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        try{
            Map<String, Object> props = new HashMap<>();
            props.put("userName", user.getFullName());
            props.put("newPassword", newPassword);
            MailRequest mailRequest = MailRequest.builder()
                    .to(user.getEmail())
                    .subject(MailUtil.SEND_EMAIL_SUBJECT.CLIENT_NEW_PASSWORD)
                    .props(props)
                    .build();
            mailService.sendHtmlMail(mailRequest, MailUtil.TEMPLATE_EMAIL_CRETE_USER.TEMPLATE_SEND_NEW_PASSWORD);
        }catch (MessagingException e) {
            e.printStackTrace();
        }

    }


    private AuthResponse generateAccessTokenAndRefreshToken(User user) {
        String accessToken = jwtTokenUtil.generateToken(user, VALID_DURATION);
        String refreshToken = jwtTokenUtil.generateToken(user, REFRESHABLE_DURATION);

        AuthResponse authResponse = AuthResponse.builder()
                .isAuth(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return authResponse;
    }






}
