package com.filae.api.domain.service;

import com.filae.api.domain.entity.Establishment;
import com.filae.api.domain.repository.EstablishmentRepository;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for establishment management operations
 */
@Service
@Transactional
public class EstablishmentService {

    private static final Logger log = LogHelper.getLogger(EstablishmentService.class);

    private final EstablishmentRepository establishmentRepository;

    public EstablishmentService(EstablishmentRepository establishmentRepository) {
        this.establishmentRepository = establishmentRepository;
    }

    /**
     * Get all establishments
     */
    @Transactional(readOnly = true)
    public List<Establishment> findAll() {
        LogHelper.logMethodEntry(log, "findAll");
        List<Establishment> establishments = establishmentRepository.findAll();
        LogHelper.logMethodExit(log, "findAll", establishments.size() + " establishments");
        return establishments;
    }

    /**
     * Find establishment by ID
     */
    @Transactional(readOnly = true)
    public Optional<Establishment> findById(Long id) {
        LogHelper.logMethodEntry(log, "findById", id);
        Optional<Establishment> establishment = establishmentRepository.findById(id);
        LogHelper.logMethodExit(log, "findById", establishment.isPresent() ? "found" : "not found");
        return establishment;
    }

    /**
     * Find establishments by category
     */
    @Transactional(readOnly = true)
    public List<Establishment> findByCategory(String category) {
        LogHelper.logMethodEntry(log, "findByCategory", category);
        List<Establishment> establishments = establishmentRepository.findByCategory(category);
        LogHelper.logMethodExit(log, "findByCategory", establishments.size() + " establishments");
        return establishments;
    }

    /**
     * Find establishments by city
     */
    @Transactional(readOnly = true)
    public List<Establishment> findByCity(String city) {
        LogHelper.logMethodEntry(log, "findByCity", city);
        List<Establishment> establishments = establishmentRepository.findByCity(city);
        LogHelper.logMethodExit(log, "findByCity", establishments.size() + " establishments");
        return establishments;
    }

    /**
     * Create new establishment
     */
    public Establishment createEstablishment(Establishment establishment) {
        LogHelper.logMethodEntry(log, "createEstablishment", establishment.getName());
        LogHelper.logOperation(log, "Creating establishment", "name=" + establishment.getName());

        Establishment saved = establishmentRepository.save(establishment);
        LogHelper.logDatabaseOperation(log, "INSERT Establishment", saved.getId());
        LogHelper.logMethodExit(log, "createEstablishment", saved.getId());

        return saved;
    }

    /**
     * Update establishment
     */
    public Establishment updateEstablishment(Long id, Establishment updates) {
        LogHelper.logMethodEntry(log, "updateEstablishment", id);

        Establishment establishment = establishmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Establishment not found with id: " + id));

        // Update fields
        if (updates.getName() != null) {
            establishment.setName(updates.getName());
        }
        if (updates.getDescription() != null) {
            establishment.setDescription(updates.getDescription());
        }
        if (updates.getCategory() != null) {
            establishment.setCategory(updates.getCategory());
        }
        if (updates.getAddress() != null) {
            establishment.setAddress(updates.getAddress());
        }
        if (updates.getCity() != null) {
            establishment.setCity(updates.getCity());
        }
        if (updates.getState() != null) {
            establishment.setState(updates.getState());
        }
        if (updates.getPhoneNumber() != null) {
            establishment.setPhoneNumber(updates.getPhoneNumber());
        }
        if (updates.getIsAcceptingCustomers() != null) {
            establishment.setIsAcceptingCustomers(updates.getIsAcceptingCustomers());
        }
        if (updates.getQueueEnabled() != null) {
            establishment.setQueueEnabled(updates.getQueueEnabled());
        }

        Establishment updated = establishmentRepository.save(establishment);
        LogHelper.logDatabaseOperation(log, "UPDATE Establishment", updated.getId());
        LogHelper.logMethodExit(log, "updateEstablishment", updated.getId());

        return updated;
    }

    /**
     * Delete establishment
     */
    public void deleteEstablishment(Long id) {
        LogHelper.logMethodEntry(log, "deleteEstablishment", id);

        if (!establishmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Establishment not found with id: " + id);
        }

        establishmentRepository.deleteById(id);
        LogHelper.logDatabaseOperation(log, "DELETE Establishment", id);
        LogHelper.logMethodExit(log, "deleteEstablishment");
    }
}

