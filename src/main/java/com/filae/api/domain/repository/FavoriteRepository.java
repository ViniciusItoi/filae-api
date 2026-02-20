package com.filae.api.domain.repository;

import com.filae.api.domain.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Favorite entity
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserIdOrderByAddedAtDesc(Long userId);

    Optional<Favorite> findByUserIdAndEstablishmentId(Long userId, Long establishmentId);

    boolean existsByUserIdAndEstablishmentId(Long userId, Long establishmentId);

    void deleteByUserIdAndEstablishmentId(Long userId, Long establishmentId);

    Long countByUserId(Long userId);
}

