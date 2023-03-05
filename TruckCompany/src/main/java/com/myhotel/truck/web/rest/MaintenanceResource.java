package com.myhotel.truck.web.rest;

import com.myhotel.truck.repository.MaintenanceRepository;
import com.myhotel.truck.service.MaintenanceQueryService;
import com.myhotel.truck.service.MaintenanceService;
import com.myhotel.truck.service.criteria.MaintenanceCriteria;
import com.myhotel.truck.service.dto.MaintenanceDTO;
import com.myhotel.truck.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.myhotel.truck.domain.Maintenance}.
 */
@RestController
@RequestMapping("/api")
public class MaintenanceResource {

    private final Logger log = LoggerFactory.getLogger(MaintenanceResource.class);

    private static final String ENTITY_NAME = "maintenance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaintenanceService maintenanceService;

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceQueryService maintenanceQueryService;

    public MaintenanceResource(
        MaintenanceService maintenanceService,
        MaintenanceRepository maintenanceRepository,
        MaintenanceQueryService maintenanceQueryService
    ) {
        this.maintenanceService = maintenanceService;
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceQueryService = maintenanceQueryService;
    }

    /**
     * {@code POST  /maintenances} : Create a new maintenance.
     *
     * @param maintenanceDTO the maintenanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maintenanceDTO, or with status {@code 400 (Bad Request)} if the maintenance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maintenances")
    public ResponseEntity<MaintenanceDTO> createMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) throws URISyntaxException {
        log.debug("REST request to save Maintenance : {}", maintenanceDTO);
        if (maintenanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new maintenance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaintenanceDTO result = maintenanceService.save(maintenanceDTO);
        return ResponseEntity
            .created(new URI("/api/maintenances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /maintenances/:id} : Updates an existing maintenance.
     *
     * @param id the id of the maintenanceDTO to save.
     * @param maintenanceDTO the maintenanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintenanceDTO,
     * or with status {@code 400 (Bad Request)} if the maintenanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maintenanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/maintenances/{id}")
    public ResponseEntity<MaintenanceDTO> updateMaintenance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaintenanceDTO maintenanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Maintenance : {}, {}", id, maintenanceDTO);
        if (maintenanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintenanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintenanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MaintenanceDTO result = maintenanceService.update(maintenanceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, maintenanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /maintenances/:id} : Partial updates given fields of an existing maintenance, field will ignore if it is null
     *
     * @param id the id of the maintenanceDTO to save.
     * @param maintenanceDTO the maintenanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintenanceDTO,
     * or with status {@code 400 (Bad Request)} if the maintenanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maintenanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maintenanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/maintenances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaintenanceDTO> partialUpdateMaintenance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MaintenanceDTO maintenanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Maintenance partially : {}, {}", id, maintenanceDTO);
        if (maintenanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintenanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintenanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaintenanceDTO> result = maintenanceService.partialUpdate(maintenanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, maintenanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /maintenances} : get all the maintenances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maintenances in body.
     */
    @GetMapping("/maintenances")
    public ResponseEntity<List<MaintenanceDTO>> getAllMaintenances(
        MaintenanceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Maintenances by criteria: {}", criteria);
        Page<MaintenanceDTO> page = maintenanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maintenances/count} : count all the maintenances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/maintenances/count")
    public ResponseEntity<Long> countMaintenances(MaintenanceCriteria criteria) {
        log.debug("REST request to count Maintenances by criteria: {}", criteria);
        return ResponseEntity.ok().body(maintenanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /maintenances/:id} : get the "id" maintenance.
     *
     * @param id the id of the maintenanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maintenanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maintenances/{id}")
    public ResponseEntity<MaintenanceDTO> getMaintenance(@PathVariable Long id) {
        log.debug("REST request to get Maintenance : {}", id);
        Optional<MaintenanceDTO> maintenanceDTO = maintenanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maintenanceDTO);
    }

    /**
     * {@code DELETE  /maintenances/:id} : delete the "id" maintenance.
     *
     * @param id the id of the maintenanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maintenances/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        log.debug("REST request to delete Maintenance : {}", id);
        maintenanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
