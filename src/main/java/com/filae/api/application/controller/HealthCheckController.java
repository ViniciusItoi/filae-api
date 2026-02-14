package com.filae.api.application.controller;

import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller
 */
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private static final Logger log = LogHelper.getLogger(HealthCheckController.class);

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        LogHelper.logMethodEntry(log, "healthCheck");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Filae API");
        response.put("version", "1.0.0");
        response.put("timestamp", System.currentTimeMillis());

        LogHelper.logMethodExit(log, "healthCheck", "UP");
        return ResponseEntity.ok(response);
    }
}

