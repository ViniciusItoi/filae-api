package com.filae.api.domain.repository;

import com.filae.api.domain.entity.Establishment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Establishment entity
 */
@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {

    // Paginated methods
    Page<Establishment> findByCategory(String category, Pageable pageable);

    Page<Establishment> findByCity(String city, Pageable pageable);

    Page<Establishment> findByMerchantId(Long merchantId, Pageable pageable);

    @Query("SELECT e FROM Establishment e WHERE " +
           "LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Establishment> search(@Param("query") String query, Pageable pageable);

    // Non-paginated methods for simple queries
    List<Establishment> findByCategory(String category);

    List<Establishment> findByCity(String city);

    List<Establishment> findByNameContainingIgnoreCase(String name);

    List<Establishment> findByQueueEnabledTrueAndIsAcceptingCustomersTrue();
}
