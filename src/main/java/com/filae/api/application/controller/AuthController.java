package com.filae.api.application.controller;

import com.filae.api.application.dto.auth.LoginRequest;
import com.filae.api.application.dto.auth.LoginResponse;
import com.filae.api.application.dto.auth.RegisterRequest;
import com.filae.api.domain.service.AuthService;
import com.filae.api.infrastructure.logging.LogHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication endpoints
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LogHelper.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LogHelper.logMethodEntry(log, "register", request.getEmail());

        try {
            LoginResponse response = authService.register(request);
            LogHelper.logMethodExit(log, "register", "success");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            LogHelper.logError(log, "register", e, request.getEmail());
            throw e;
        }
    }

    /**
     * Login user
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LogHelper.logMethodEntry(log, "login", request.getEmail());

        try {
            LoginResponse response = authService.login(request);
            LogHelper.logMethodExit(log, "login", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LogHelper.logError(log, "login", e, request.getEmail());
            throw e;
        }
    }
}

