package com.myhotel.company.web.rest;

import com.myhotel.company.repository.LocationsRepository;
import com.myhotel.company.service.LocationsQueryService;
import com.myhotel.company.service.LocationsService;
import com.myhotel.company.service.criteria.LocationsCriteria;
import com.myhotel.company.service.dto.LocationsDTO;
import com.myhotel.company.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myhotel.company.domain.Locations}.
 */
@RestController
@RequestMapping("/api")
public class LocationsResource {

    private final Logger log = LoggerFactory.getLogger(LocationsResource.class);

    private static final String ENTITY_NAME = "locations";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationsService locationsService;

    private final LocationsRepository locationsRepository;

    private final LocationsQueryService locationsQueryService;

    public LocationsResource(
        LocationsService locationsService,
        LocationsRepository locationsRepository,
        LocationsQueryService locationsQueryService
    ) {
        this.locationsService = locationsService;
        this.locationsRepository = locationsRepository;
        this.locationsQueryService = locationsQueryService;
    }

    /**
     * {@code POST  /locations} : Create a new locations.
     *
     * @param locationsDTO the locationsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationsDTO, or with status {@code 400 (Bad Request)} if the locations has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locations")
    public ResponseEntity<LocationsDTO> createLocations(@RequestBody LocationsDTO locationsDTO) throws URISyntaxException {
        log.debug("REST request to save Locations : {}", locationsDTO);
        if (locationsDTO.getId() != null) {
            throw new BadRequestAlertException("A new locations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationsDTO result = locationsService.save(locationsDTO);
        return ResponseEntity
            .created(new URI("/api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locations/:id} : Updates an existing locations.
     *
     * @param id the id of the locationsDTO to save.
     * @param locationsDTO the locationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationsDTO,
     * or with status {@code 400 (Bad Request)} if the locationsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locations/{id}")
    public ResponseEntity<LocationsDTO> updateLocations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationsDTO locationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Locations : {}, {}", id, locationsDTO);
        if (locationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationsDTO result = locationsService.update(locationsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /locations/:id} : Partial updates given fields of an existing locations, field will ignore if it is null
     *
     * @param id the id of the locationsDTO to save.
     * @param locationsDTO the locationsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationsDTO,
     * or with status {@code 400 (Bad Request)} if the locationsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the locationsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/locations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocationsDTO> partialUpdateLocations(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationsDTO locationsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Locations partially : {}, {}", id, locationsDTO);
        if (locationsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationsDTO> result = locationsService.partialUpdate(locationsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /locations} : get all the locations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locations in body.
     */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationsDTO>> getAllLocations(
        LocationsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Locations by criteria: {}", criteria);
        Page<LocationsDTO> page = locationsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /locations/count} : count all the locations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/locations/count")
    public ResponseEntity<Long> countLocations(LocationsCriteria criteria) {
        log.debug("REST request to count Locations by criteria: {}", criteria);
        return ResponseEntity.ok().body(locationsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /locations/:id} : get the "id" locations.
     *
     * @param id the id of the locationsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationsDTO> getLocations(@PathVariable Long id) {
        log.debug("REST request to get Locations : {}", id);
        Optional<LocationsDTO> locationsDTO = locationsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationsDTO);
    }

    /**
     * {@code DELETE  /locations/:id} : delete the "id" locations.
     *
     * @param id the id of the locationsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocations(@PathVariable Long id) {
        log.debug("REST request to delete Locations : {}", id);
        locationsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
