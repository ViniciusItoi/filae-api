package com.filae.api.domain.service;

import com.filae.api.domain.entity.Establishment;
import com.filae.api.domain.entity.Queue;
import com.filae.api.domain.entity.User;
import com.filae.api.domain.repository.EstablishmentRepository;
import com.filae.api.domain.repository.QueueRepository;
import com.filae.api.domain.repository.UserRepository;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for queue management operations
 */
@Service
@Transactional
public class QueueService {

    private static final Logger log = LogHelper.getLogger(QueueService.class);

    private final QueueRepository queueRepository;
    private final EstablishmentRepository establishmentRepository;
    private final UserRepository userRepository;

    public QueueService(QueueRepository queueRepository,
                       EstablishmentRepository establishmentRepository,
                       UserRepository userRepository) {
        this.queueRepository = queueRepository;
        this.establishmentRepository = establishmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * Join a queue
     */
    public Queue joinQueue(Long establishmentId, Long userId, Integer partySize, String notes) {
        LogHelper.logMethodEntry(log, "joinQueue", establishmentId, userId, partySize);
        LogHelper.logOperation(log, "User joining queue",
            "establishment=" + establishmentId, "user=" + userId, "partySize=" + partySize);

        // Validate establishment
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new IllegalArgumentException("Establishment not found with id: " + establishmentId));

        if (!establishment.getQueueEnabled()) {
            throw new IllegalStateException("Queue is not enabled for this establishment");
        }

        if (!establishment.getIsAcceptingCustomers()) {
            throw new IllegalStateException("Establishment is not currently accepting customers");
        }

        // Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Check if user already in this queue
        Optional<Queue> existingQueue = queueRepository.findActiveQueuesByUserId(
                userId, Queue.QueueStatus.WAITING).stream()
                .filter(q -> q.getEstablishment().getId().equals(establishmentId))
                .findFirst();

        if (existingQueue.isPresent()) {
            throw new IllegalStateException("User is already in this queue");
        }

        // Calculate position and total
        List<Queue> activeQueues = queueRepository.findByEstablishmentIdAndStatusOrderByPosition(
                establishmentId, Queue.QueueStatus.WAITING);

        int position = activeQueues.size() + 1;
        int totalInQueue = activeQueues.size() + 1;

        // Generate unique ticket number
        String ticketNumber = generateTicketNumber(establishment);

        // Estimate wait time (simple: 10 minutes per position)
        int estimatedWaitTime = position * 10;

        // Create queue entry
        Queue queue = Queue.builder()
                .ticketNumber(ticketNumber)
                .establishment(establishment)
                .user(user)
                .partySize(partySize != null ? partySize : 1)
                .notes(notes)
                .position(position)
                .totalInQueue(totalInQueue)
                .status(Queue.QueueStatus.WAITING)
                .estimatedWaitTime(estimatedWaitTime)
                .build();

        Queue saved = queueRepository.save(queue);
        LogHelper.logDatabaseOperation(log, "INSERT Queue", saved.getId());
        LogHelper.logOperation(log, "Queue joined",
            "ticket=" + ticketNumber, "position=" + position);
        LogHelper.logMethodExit(log, "joinQueue", saved.getId());

        return saved;
    }

    /**
     * Get user's queue entries
     */
    @Transactional(readOnly = true)
    public List<Queue> getUserQueues(Long userId) {
        LogHelper.logMethodEntry(log, "getUserQueues", userId);
        List<Queue> queues = queueRepository.findActiveQueuesByUserId(userId, Queue.QueueStatus.WAITING);
        LogHelper.logMethodExit(log, "getUserQueues", queues.size() + " queues");
        return queues;
    }

    /**
     * Get establishment's queue
     */
    @Transactional(readOnly = true)
    public List<Queue> getEstablishmentQueue(Long establishmentId) {
        LogHelper.logMethodEntry(log, "getEstablishmentQueue", establishmentId);
        List<Queue> queues = queueRepository.findByEstablishmentIdAndStatusOrderByPosition(
                establishmentId, Queue.QueueStatus.WAITING);
        LogHelper.logMethodExit(log, "getEstablishmentQueue", queues.size() + " in queue");
        return queues;
    }

    /**
     * Get queue by ID
     */
    @Transactional(readOnly = true)
    public Optional<Queue> getQueueById(Long id) {
        LogHelper.logMethodEntry(log, "getQueueById", id);
        Optional<Queue> queue = queueRepository.findById(id);
        LogHelper.logMethodExit(log, "getQueueById", queue.isPresent() ? "found" : "not found");
        return queue;
    }

    /**
     * Cancel queue entry (Customer)
     */
    public void cancelQueue(Long queueId, Long userId) {
        LogHelper.logMethodEntry(log, "cancelQueue", queueId, userId);

        Queue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new IllegalArgumentException("Queue entry not found with id: " + queueId));

        // Verify ownership
        if (!queue.getUser().getId().equals(userId)) {
            throw new IllegalStateException("User does not own this queue entry");
        }

        // Can only cancel WAITING queues
        if (queue.getStatus() != Queue.QueueStatus.WAITING) {
            throw new IllegalStateException("Can only cancel queues with WAITING status");
        }

        queue.setStatus(Queue.QueueStatus.CANCELLED);
        queue.setCancelledAt(LocalDateTime.now());
        queueRepository.save(queue);

        // Update positions for remaining queues
        updateQueuePositions(queue.getEstablishment().getId());

        LogHelper.logDatabaseOperation(log, "UPDATE Queue - CANCELLED", queueId);
        LogHelper.logOperation(log, "Queue cancelled", "ticket=" + queue.getTicketNumber());
        LogHelper.logMethodExit(log, "cancelQueue");
    }

    /**
     * Call next customer in queue (Merchant)
     */
    public Queue callNext(Long establishmentId) {
        LogHelper.logMethodEntry(log, "callNext", establishmentId);

        List<Queue> waitingQueues = queueRepository.findByEstablishmentIdAndStatusOrderByPosition(
                establishmentId, Queue.QueueStatus.WAITING);

        if (waitingQueues.isEmpty()) {
            throw new IllegalStateException("No customers in queue");
        }

        Queue nextQueue = waitingQueues.get(0);
        nextQueue.setStatus(Queue.QueueStatus.CALLED);
        nextQueue.setCalledAt(LocalDateTime.now());
        Queue updated = queueRepository.save(nextQueue);

        // Update positions for remaining queues
        updateQueuePositions(establishmentId);

        LogHelper.logDatabaseOperation(log, "UPDATE Queue - CALLED", updated.getId());
        LogHelper.logOperation(log, "Customer called", "ticket=" + updated.getTicketNumber());
        LogHelper.logMethodExit(log, "callNext", updated.getId());

        return updated;
    }

    /**
     * Mark queue as finished (Merchant)
     */
    public void finishQueue(Long queueId) {
        LogHelper.logMethodEntry(log, "finishQueue", queueId);

        Queue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new IllegalArgumentException("Queue entry not found with id: " + queueId));

        queue.setStatus(Queue.QueueStatus.FINISHED);
        queue.setFinishedAt(LocalDateTime.now());
        queueRepository.save(queue);

        LogHelper.logDatabaseOperation(log, "UPDATE Queue - FINISHED", queueId);
        LogHelper.logOperation(log, "Queue finished", "ticket=" + queue.getTicketNumber());
        LogHelper.logMethodExit(log, "finishQueue");
    }

    /**
     * Update queue positions after cancellation or call
     */
    private void updateQueuePositions(Long establishmentId) {
        List<Queue> waitingQueues = queueRepository.findByEstablishmentIdAndStatusOrderByPosition(
                establishmentId, Queue.QueueStatus.WAITING);

        int position = 1;
        for (Queue queue : waitingQueues) {
            queue.setPosition(position);
            queue.setTotalInQueue(waitingQueues.size());
            queue.setEstimatedWaitTime(position * 10); // Recalculate wait time
            position++;
        }

        queueRepository.saveAll(waitingQueues);
    }

    /**
     * Generate unique ticket number
     */
    private String generateTicketNumber(Establishment establishment) {
        // Format: First 2 letters of establishment + random 4 chars
        String prefix = establishment.getName().substring(0, Math.min(2, establishment.getName().length())).toUpperCase();
        String suffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return prefix + "-" + suffix;
    }
}

