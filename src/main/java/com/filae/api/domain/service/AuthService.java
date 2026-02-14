package com.filae.api.domain.service;

import com.filae.api.application.dto.auth.LoginRequest;
import com.filae.api.application.dto.auth.LoginResponse;
import com.filae.api.application.dto.auth.RegisterRequest;
import com.filae.api.domain.entity.User;
import com.filae.api.infrastructure.logging.LogHelper;
import com.filae.api.infrastructure.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication operations
 */
@Service
@Transactional
public class AuthService {

    private static final Logger log = LogHelper.getLogger(AuthService.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserService userService,
                      AuthenticationManager authenticationManager,
                      JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Register a new user
     */
    public LoginResponse register(RegisterRequest request) {
        LogHelper.logMethodEntry(log, "register", request.getEmail());
        LogHelper.logOperation(log, "User registration", "email=" + request.getEmail(), "type=" + request.getUserType());

        // Create user entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(request.getPassword()) // Will be hashed by UserService
                .phone(request.getPhone())
                .userType(request.getUserType())
                .isActive(true)
                .build();

        // Save user
        User savedUser = userService.createUser(user);

        // Generate JWT token
        String token = tokenProvider.generateToken(savedUser.getEmail());

        LogHelper.logSecurityEvent(log, "User registered", savedUser.getEmail());
        LogHelper.logMethodExit(log, "register", savedUser.getId());

        return new LoginResponse(
                token,
                "Bearer",
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getUserType().name()
        );
    }

    /**
     * Login user
     */
    public LoginResponse login(LoginRequest request) {
        LogHelper.logMethodEntry(log, "login", request.getEmail());
        LogHelper.logOperation(log, "User login attempt", "email=" + request.getEmail());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = tokenProvider.generateToken(authentication);

        // Get user details
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LogHelper.logSecurityEvent(log, "Login successful", user.getEmail());
        LogHelper.logMethodExit(log, "login", user.getId());

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getUserType().name()
        );
    }
}

