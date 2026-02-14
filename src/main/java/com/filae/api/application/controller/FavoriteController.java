package com.filae.api.application.controller;

import com.filae.api.application.dto.favorite.AddFavoriteRequest;
import com.filae.api.application.dto.favorite.FavoriteResponse;
import com.filae.api.application.mapper.FavoriteMapper;
import com.filae.api.domain.service.FavoriteService;
import com.filae.api.domain.service.UserService;
import com.filae.api.infrastructure.logging.LogHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for favorite management endpoints
 */
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private static final Logger log = LogHelper.getLogger(FavoriteController.class);

    private final FavoriteService favoriteService;
    private final UserService userService;
    private final FavoriteMapper favoriteMapper;

    public FavoriteController(FavoriteService favoriteService, UserService userService, FavoriteMapper favoriteMapper) {
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.favoriteMapper = favoriteMapper;
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
     * Add establishment to favorites
     */
    @PostMapping
    public ResponseEntity<FavoriteResponse> addFavorite(@Valid @RequestBody AddFavoriteRequest request) {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "addFavorite", userId, request.getEstablishmentId());

        var favorite = favoriteService.addFavorite(userId, request.getEstablishmentId());

        LogHelper.logMethodExit(log, "addFavorite", favorite.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteMapper.toResponse(favorite));
    }

    /**
     * Get user's favorites
     */
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites() {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "getFavorites", userId);

        List<FavoriteResponse> favorites = favoriteService.getUserFavorites(userId)
            .stream()
            .map(favoriteMapper::toResponse)
            .collect(Collectors.toList());

        LogHelper.logMethodExit(log, "getFavorites", favorites.size() + " found");
        return ResponseEntity.ok(favorites);
    }

    /**
     * Remove from favorites
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "removeFavorite", userId, id);

        favoriteService.removeFavorite(userId, id);

        LogHelper.logMethodExit(log, "removeFavorite");
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if establishment is favorited
     */
    @GetMapping("/check/{establishmentId}")
    public ResponseEntity<Boolean> isFavorited(@PathVariable Long establishmentId) {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "isFavorited", userId, establishmentId);

        boolean isFavorited = favoriteService.isFavorited(userId, establishmentId);

        LogHelper.logMethodExit(log, "isFavorited", isFavorited ? "yes" : "no");
        return ResponseEntity.ok(isFavorited);
    }
}

