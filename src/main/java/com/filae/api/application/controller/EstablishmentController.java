package com.filae.api.application.controller;

import com.filae.api.domain.entity.Establishment;
import com.filae.api.domain.service.EstablishmentService;
import com.filae.api.infrastructure.logging.LogHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for establishment endpoints
 */
@RestController
@RequestMapping("/establishments")
public class EstablishmentController {

    private static final Logger log = LogHelper.getLogger(EstablishmentController.class);

    private final EstablishmentService establishmentService;

    public EstablishmentController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    /**
     * Get all establishments
     */
    @GetMapping
    public ResponseEntity<List<Establishment>> getAllEstablishments(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city) {
        LogHelper.logMethodEntry(log, "getAllEstablishments", category, city);

        List<Establishment> establishments;

        if (category != null && !category.isEmpty()) {
            establishments = establishmentService.findByCategory(category);
        } else if (city != null && !city.isEmpty()) {
            establishments = establishmentService.findByCity(city);
        } else {
            establishments = establishmentService.findAll();
        }

        LogHelper.logMethodExit(log, "getAllEstablishments", establishments.size() + " found");
        return ResponseEntity.ok(establishments);
    }

    /**
     * Get establishment by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Establishment> getEstablishmentById(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "getEstablishmentById", id);

        Establishment establishment = establishmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Establishment not found with id: " + id));

        LogHelper.logMethodExit(log, "getEstablishmentById", establishment.getName());
        return ResponseEntity.ok(establishment);
    }

    /**
     * Create new establishment
     */
    @PostMapping
    public ResponseEntity<Establishment> createEstablishment(@Valid @RequestBody Establishment establishment) {
        LogHelper.logMethodEntry(log, "createEstablishment", establishment.getName());

        Establishment created = establishmentService.createEstablishment(establishment);

        LogHelper.logMethodExit(log, "createEstablishment", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update establishment
     */
    @PutMapping("/{id}")
    public ResponseEntity<Establishment> updateEstablishment(
            @PathVariable Long id,
            @Valid @RequestBody Establishment establishment) {
        LogHelper.logMethodEntry(log, "updateEstablishment", id);

        Establishment updated = establishmentService.updateEstablishment(id, establishment);

        LogHelper.logMethodExit(log, "updateEstablishment", updated.getId());
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete establishment
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstablishment(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "deleteEstablishment", id);

        establishmentService.deleteEstablishment(id);

        LogHelper.logMethodExit(log, "deleteEstablishment");
        return ResponseEntity.noContent().build();
    }
}

