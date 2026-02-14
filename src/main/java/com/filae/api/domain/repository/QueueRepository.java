package com.filae.api.domain.repository;

import com.filae.api.domain.entity.Queue;
import com.filae.api.domain.entity.Queue.QueueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Queue entity
 */
@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {

    Optional<Queue> findByTicketNumber(String ticketNumber);

    List<Queue> findByEstablishmentIdAndStatusOrderByPositionAsc(Long establishmentId, QueueStatus status);

    List<Queue> findByEstablishmentIdAndStatusOrderByPosition(Long establishmentId, QueueStatus status);

    Page<Queue> findByUserIdOrderByJoinedAtDesc(Long userId, Pageable pageable);

    List<Queue> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Queue> findByUserIdAndEstablishmentIdAndStatus(Long userId, Long establishmentId, QueueStatus status);

    @Query("SELECT q FROM Queue q WHERE q.user.id = :userId AND q.status = :status")
    List<Queue> findActiveQueuesByUserId(@Param("userId") Long userId, @Param("status") QueueStatus status);

    @Query("SELECT COUNT(q) FROM Queue q WHERE q.establishment.id = :establishmentId AND q.status = :status")
    Integer countByEstablishmentAndStatus(@Param("establishmentId") Long establishmentId, @Param("status") QueueStatus status);

    @Query("SELECT q FROM Queue q WHERE q.establishment.id = :establishmentId AND q.status IN ('WAITING', 'CALLED') ORDER BY q.position ASC")
    List<Queue> findActiveQueuesForEstablishment(@Param("establishmentId") Long establishmentId);

    boolean existsByUserIdAndEstablishmentIdAndStatusIn(Long userId, Long establishmentId, List<QueueStatus> statuses);
}
