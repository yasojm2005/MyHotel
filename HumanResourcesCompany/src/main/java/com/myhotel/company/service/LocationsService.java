package com.myhotel.company.service;

import com.myhotel.company.service.dto.LocationsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myhotel.company.domain.Locations}.
 */
public interface LocationsService {
    /**
     * Save a locations.
     *
     * @param locationsDTO the entity to save.
     * @return the persisted entity.
     */
    LocationsDTO save(LocationsDTO locationsDTO);

    /**
     * Updates a locations.
     *
     * @param locationsDTO the entity to update.
     * @return the persisted entity.
     */
    LocationsDTO update(LocationsDTO locationsDTO);

    /**
     * Partially updates a locations.
     *
     * @param locationsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LocationsDTO> partialUpdate(LocationsDTO locationsDTO);

    /**
     * Get all the locations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LocationsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" locations.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocationsDTO> findOne(Long id);

    /**
     * Delete the "id" locations.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
