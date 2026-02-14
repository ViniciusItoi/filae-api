package com.filae.api.domain.service;

import com.filae.api.domain.entity.User;
import com.filae.api.domain.repository.UserRepository;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service for user management operations
 */
@Service
@Transactional
public class UserService {

    private static final Logger log = LogHelper.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new user
     */
    public User createUser(User user) {
        LogHelper.logMethodEntry(log, "createUser", user.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            LogHelper.logValidationError(log, "email", "Email already exists");
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // Hash password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        User savedUser = userRepository.save(user);
        LogHelper.logDatabaseOperation(log, "INSERT User", savedUser.getId());
        LogHelper.logMethodExit(log, "createUser", savedUser.getId());

        return savedUser;
    }

    /**
     * Find user by email
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        LogHelper.logMethodEntry(log, "findByEmail", email);
        Optional<User> user = userRepository.findByEmail(email);
        LogHelper.logMethodExit(log, "findByEmail", user.isPresent() ? "found" : "not found");
        return user;
    }

    /**
     * Find user by ID
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        LogHelper.logMethodEntry(log, "findById", id);
        Optional<User> user = userRepository.findById(id);
        LogHelper.logMethodExit(log, "findById", user.isPresent() ? "found" : "not found");
        return user;
    }

    /**
     * Update user
     */
    public User updateUser(Long id, User userUpdates) {
        LogHelper.logMethodEntry(log, "updateUser", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Update allowed fields
        if (userUpdates.getName() != null) {
            user.setName(userUpdates.getName());
        }
        if (userUpdates.getPhone() != null) {
            user.setPhone(userUpdates.getPhone());
        }
        if (userUpdates.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(userUpdates.getProfilePictureUrl());
        }

        User updatedUser = userRepository.save(user);
        LogHelper.logDatabaseOperation(log, "UPDATE User", updatedUser.getId());
        LogHelper.logMethodExit(log, "updateUser", updatedUser.getId());

        return updatedUser;
    }

    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        LogHelper.logMethodEntry(log, "deleteUser", id);

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        LogHelper.logDatabaseOperation(log, "DELETE User", id);
        LogHelper.logMethodExit(log, "deleteUser");
    }
}

