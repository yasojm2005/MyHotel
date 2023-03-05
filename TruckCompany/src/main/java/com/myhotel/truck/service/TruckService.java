package com.myhotel.truck.service;

import com.myhotel.truck.service.dto.TruckDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myhotel.truck.domain.Truck}.
 */
public interface TruckService {
    /**
     * Save a truck.
     *
     * @param truckDTO the entity to save.
     * @return the persisted entity.
     */
    TruckDTO save(TruckDTO truckDTO);

    /**
     * Updates a truck.
     *
     * @param truckDTO the entity to update.
     * @return the persisted entity.
     */
    TruckDTO update(TruckDTO truckDTO);

    /**
     * Partially updates a truck.
     *
     * @param truckDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TruckDTO> partialUpdate(TruckDTO truckDTO);

    /**
     * Get all the trucks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TruckDTO> findAll(Pageable pageable);

    /**
     * Get the "id" truck.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TruckDTO> findOne(Long id);

    /**
     * Delete the "id" truck.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
