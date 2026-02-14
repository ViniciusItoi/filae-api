package com.filae.api.application.controller;

import com.filae.api.application.dto.user.UpdateUserRequest;
import com.filae.api.application.dto.user.UserResponse;
import com.filae.api.application.mapper.UserMapper;
import com.filae.api.domain.entity.User;
import com.filae.api.domain.service.UserService;
import com.filae.api.infrastructure.logging.LogHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user profile endpoints
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LogHelper.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Get authenticated user ID from security context
     */
    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            return userService.getUserByEmail(email).getId();
        }
        throw new IllegalStateException("User is not authenticated");
    }

    /**
     * Get current user profile
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "getCurrentUser", userId);

        User user = userService.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        LogHelper.logMethodExit(log, "getCurrentUser", user.getEmail());
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    /**
     * Get user by ID (if authorized)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "getUserById", id);

        User user = userService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        LogHelper.logMethodExit(log, "getUserById", user.getEmail());
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    /**
     * Update current user profile
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request) {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "updateCurrentUser", userId);

        User userUpdates = User.builder()
            .name(request.getName())
            .phone(request.getPhone())
            .profilePictureUrl(request.getProfilePictureUrl())
            .build();

        User updatedUser = userService.updateUser(userId, userUpdates);

        LogHelper.logMethodExit(log, "updateCurrentUser", updatedUser.getId());
        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    /**
     * Update user by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        LogHelper.logMethodEntry(log, "updateUser", id);

        User userUpdates = User.builder()
            .name(request.getName())
            .phone(request.getPhone())
            .profilePictureUrl(request.getProfilePictureUrl())
            .build();

        User updatedUser = userService.updateUser(id, userUpdates);

        LogHelper.logMethodExit(log, "updateUser", updatedUser.getId());
        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    /**
     * Delete current user account
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentUser() {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "deleteCurrentUser", userId);

        userService.deleteUser(userId);

        LogHelper.logMethodExit(log, "deleteCurrentUser");
        return ResponseEntity.noContent().build();
    }
}

