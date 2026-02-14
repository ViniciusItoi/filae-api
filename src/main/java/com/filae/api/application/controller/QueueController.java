package com.filae.api.application.controller;

import com.filae.api.domain.entity.Queue;
import com.filae.api.domain.service.QueueService;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for queue management endpoints
 */
@RestController
@RequestMapping("/queues")
public class QueueController {

    private static final Logger log = LogHelper.getLogger(QueueController.class);

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    /**
     * Join a queue
     */
    @PostMapping("/join")
    public ResponseEntity<Queue> joinQueue(@RequestBody Map<String, Object> request) {

        Long establishmentId = Long.valueOf(request.get("establishmentId").toString());
        Integer partySize = request.containsKey("partySize") ?
            Integer.parseInt(request.get("partySize").toString()) : 1;
        String notes = request.containsKey("notes") ? request.get("notes").toString() : null;

        // Get user ID from authentication (simplified - in real app would look up user by email)
        Long userId = 1L; // TODO: Get from authenticated user

        LogHelper.logMethodEntry(log, "joinQueue", establishmentId);

        Queue queue = queueService.joinQueue(establishmentId, userId, partySize, notes);

        LogHelper.logMethodExit(log, "joinQueue", queue.getTicketNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(queue);
    }

    /**
     * Get user's queue entries
     */
    @GetMapping("/my-queues")
    public ResponseEntity<List<Queue>> getMyQueues() {
        LogHelper.logMethodEntry(log, "getMyQueues");

        // Get user ID from authentication
        Long userId = 1L; // TODO: Get from authenticated user

        List<Queue> queues = queueService.getUserQueues(userId);

        LogHelper.logMethodExit(log, "getMyQueues", queues.size() + " queues");
        return ResponseEntity.ok(queues);
    }

    /**
     * Get queue by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Queue> getQueueById(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "getQueueById", id);

        Queue queue = queueService.getQueueById(id)
                .orElseThrow(() -> new IllegalArgumentException("Queue not found with id: " + id));

        LogHelper.logMethodExit(log, "getQueueById", queue.getTicketNumber());
        return ResponseEntity.ok(queue);
    }

    /**
     * Get establishment's queue
     */
    @GetMapping("/establishment/{establishmentId}")
    public ResponseEntity<List<Queue>> getEstablishmentQueue(@PathVariable Long establishmentId) {
        LogHelper.logMethodEntry(log, "getEstablishmentQueue", establishmentId);

        List<Queue> queues = queueService.getEstablishmentQueue(establishmentId);

        LogHelper.logMethodExit(log, "getEstablishmentQueue", queues.size() + " in queue");
        return ResponseEntity.ok(queues);
    }

    /**
     * Cancel queue entry
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelQueue(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "cancelQueue", id);

        // Get user ID from authentication
        Long userId = 1L; // TODO: Get from authenticated user

        queueService.cancelQueue(id, userId);

        LogHelper.logMethodExit(log, "cancelQueue");
        return ResponseEntity.noContent().build();
    }

    /**
     * Call next customer (Merchant only)
     */
    @PutMapping("/establishment/{establishmentId}/call-next")
    public ResponseEntity<Queue> callNext(@PathVariable Long establishmentId) {
        LogHelper.logMethodEntry(log, "callNext", establishmentId);

        Queue queue = queueService.callNext(establishmentId);

        LogHelper.logMethodExit(log, "callNext", queue.getTicketNumber());
        return ResponseEntity.ok(queue);
    }

    /**
     * Mark queue as finished (Merchant only)
     */
    @PutMapping("/{id}/finish")
    public ResponseEntity<Void> finishQueue(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "finishQueue", id);

        queueService.finishQueue(id);

        LogHelper.logMethodExit(log, "finishQueue");
        return ResponseEntity.noContent().build();
    }
}

