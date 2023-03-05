package com.myhotel.truck.service;

import com.myhotel.truck.service.dto.MaintenanceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myhotel.truck.domain.Maintenance}.
 */
public interface MaintenanceService {
    /**
     * Save a maintenance.
     *
     * @param maintenanceDTO the entity to save.
     * @return the persisted entity.
     */
    MaintenanceDTO save(MaintenanceDTO maintenanceDTO);

    /**
     * Updates a maintenance.
     *
     * @param maintenanceDTO the entity to update.
     * @return the persisted entity.
     */
    MaintenanceDTO update(MaintenanceDTO maintenanceDTO);

    /**
     * Partially updates a maintenance.
     *
     * @param maintenanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaintenanceDTO> partialUpdate(MaintenanceDTO maintenanceDTO);

    /**
     * Get all the maintenances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaintenanceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" maintenance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaintenanceDTO> findOne(Long id);

    /**
     * Delete the "id" maintenance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
