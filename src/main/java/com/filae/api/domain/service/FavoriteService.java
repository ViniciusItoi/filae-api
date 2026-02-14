package com.filae.api.domain.service;

import com.filae.api.domain.entity.Favorite;
import com.filae.api.domain.entity.User;
import com.filae.api.domain.entity.Establishment;
import com.filae.api.domain.repository.FavoriteRepository;
import com.filae.api.domain.repository.UserRepository;
import com.filae.api.domain.repository.EstablishmentRepository;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for favorite management operations
 */
@Service
@Transactional
public class FavoriteService {

    private static final Logger log = LogHelper.getLogger(FavoriteService.class);

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                          UserRepository userRepository,
                          EstablishmentRepository establishmentRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.establishmentRepository = establishmentRepository;
    }

    /**
     * Add establishment to favorites
     */
    public Favorite addFavorite(Long userId, Long establishmentId) {
        LogHelper.logMethodEntry(log, "addFavorite", userId, establishmentId);

        // Check if already favorited
        if (favoriteRepository.existsByUserIdAndEstablishmentId(userId, establishmentId)) {
            throw new IllegalStateException("Establishment is already in favorites");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Establishment establishment = establishmentRepository.findById(establishmentId)
            .orElseThrow(() -> new IllegalArgumentException("Establishment not found with id: " + establishmentId));

        Favorite favorite = Favorite.builder()
            .user(user)
            .establishment(establishment)
            .build();

        Favorite saved = favoriteRepository.save(favorite);
        LogHelper.logDatabaseOperation(log, "INSERT Favorite", saved.getId());
        LogHelper.logMethodExit(log, "addFavorite", saved.getId());

        return saved;
    }

    /**
     * Get user's favorites
     */
    @Transactional(readOnly = true)
    public List<Favorite> getUserFavorites(Long userId) {
        LogHelper.logMethodEntry(log, "getUserFavorites", userId);

        List<Favorite> favorites = favoriteRepository.findByUserIdOrderByAddedAtDesc(userId);
        LogHelper.logMethodExit(log, "getUserFavorites", favorites.size() + " found");

        return favorites;
    }

    /**
     * Remove from favorites
     */
    public void removeFavorite(Long userId, Long favoriteId) {
        LogHelper.logMethodEntry(log, "removeFavorite", userId, favoriteId);

        Favorite favorite = favoriteRepository.findById(favoriteId)
            .orElseThrow(() -> new IllegalArgumentException("Favorite not found with id: " + favoriteId));

        if (!favorite.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this favorite");
        }

        favoriteRepository.deleteById(favoriteId);
        LogHelper.logDatabaseOperation(log, "DELETE Favorite", favoriteId);
        LogHelper.logMethodExit(log, "removeFavorite");
    }

    /**
     * Check if establishment is favorited
     */
    @Transactional(readOnly = true)
    public boolean isFavorited(Long userId, Long establishmentId) {
        return favoriteRepository.existsByUserIdAndEstablishmentId(userId, establishmentId);
    }
}

