package com.filae.api.application.controller;

import com.filae.api.application.dto.queue.JoinQueueRequest;
import com.filae.api.application.dto.queue.QueueResponse;
import com.filae.api.application.mapper.QueueMapper;
import com.filae.api.domain.entity.Queue;
import com.filae.api.domain.service.QueueService;
import com.filae.api.domain.service.UserService;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for queue management endpoints
 */
@RestController
@RequestMapping("/queues")
public class QueueController {

    private static final Logger log = LogHelper.getLogger(QueueController.class);

    private final QueueService queueService;
    private final UserService userService;
    private final QueueMapper queueMapper;

    public QueueController(QueueService queueService, UserService userService, QueueMapper queueMapper) {
        this.queueService = queueService;
        this.userService = userService;
        this.queueMapper = queueMapper;
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
     * Join a queue
     */
    @PostMapping("/join")
    public ResponseEntity<QueueResponse> joinQueue(@RequestBody JoinQueueRequest request) {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "joinQueue", request.getEstablishmentId());

        Queue queue = queueService.joinQueue(
            request.getEstablishmentId(),
            userId,
            request.getPartySize(),
            request.getNotes()
        );

        LogHelper.logMethodExit(log, "joinQueue", queue.getTicketNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(queueMapper.toResponse(queue));
    }

    /**
     * Get user's queue entries
     */
    @GetMapping("/my-queues")
    public ResponseEntity<List<QueueResponse>> getMyQueues() {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "getMyQueues");

        List<QueueResponse> queues = queueService.getUserQueues(userId)
            .stream()
            .map(queueMapper::toResponse)
            .collect(Collectors.toList());

        LogHelper.logMethodExit(log, "getMyQueues", queues.size() + " queues");
        return ResponseEntity.ok(queues);
    }

    /**
     * Get queue by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<QueueResponse> getQueueById(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "getQueueById", id);

        Queue queue = queueService.getQueueById(id)
            .orElseThrow(() -> new IllegalArgumentException("Queue not found with id: " + id));

        LogHelper.logMethodExit(log, "getQueueById", queue.getTicketNumber());
        return ResponseEntity.ok(queueMapper.toResponse(queue));
    }

    /**
     * Get establishment's queue
     */
    @GetMapping("/establishment/{establishmentId}")
    public ResponseEntity<List<QueueResponse>> getEstablishmentQueue(@PathVariable Long establishmentId) {
        LogHelper.logMethodEntry(log, "getEstablishmentQueue", establishmentId);

        List<QueueResponse> queues = queueService.getEstablishmentQueue(establishmentId)
            .stream()
            .map(queueMapper::toResponse)
            .collect(Collectors.toList());

        LogHelper.logMethodExit(log, "getEstablishmentQueue", queues.size() + " in queue");
        return ResponseEntity.ok(queues);
    }

    /**
     * Cancel queue entry
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelQueue(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "cancelQueue", id);

        queueService.cancelQueue(id, userId);

        LogHelper.logMethodExit(log, "cancelQueue");
        return ResponseEntity.noContent().build();
    }

    /**
     * Call next customer (Merchant only)
     */
    @PutMapping("/establishment/{establishmentId}/call-next")
    public ResponseEntity<QueueResponse> callNext(@PathVariable Long establishmentId) {
        LogHelper.logMethodEntry(log, "callNext", establishmentId);

        Queue queue = queueService.callNext(establishmentId);

        LogHelper.logMethodExit(log, "callNext", queue.getTicketNumber());
        return ResponseEntity.ok(queueMapper.toResponse(queue));
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

