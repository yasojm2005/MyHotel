package com.myhotel.company.web.rest;

import com.myhotel.company.repository.DepartmentsRepository;
import com.myhotel.company.service.DepartmentsQueryService;
import com.myhotel.company.service.DepartmentsService;
import com.myhotel.company.service.criteria.DepartmentsCriteria;
import com.myhotel.company.service.dto.DepartmentsDTO;
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
 * REST controller for managing {@link com.myhotel.company.domain.Departments}.
 */
@RestController
@RequestMapping("/api")
public class DepartmentsResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentsResource.class);

    private static final String ENTITY_NAME = "departments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentsService departmentsService;

    private final DepartmentsRepository departmentsRepository;

    private final DepartmentsQueryService departmentsQueryService;

    public DepartmentsResource(
        DepartmentsService departmentsService,
        DepartmentsRepository departmentsRepository,
        DepartmentsQueryService departmentsQueryService
    ) {
        this.departmentsService = departmentsService;
        this.departmentsRepository = departmentsRepository;
        this.departmentsQueryService = departmentsQueryService;
    }

    /**
     * {@code POST  /departments} : Create a new departments.
     *
     * @param departmentsDTO the departmentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentsDTO, or with status {@code 400 (Bad Request)} if the departments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/departments")
    public ResponseEntity<DepartmentsDTO> createDepartments(@RequestBody DepartmentsDTO departmentsDTO) throws URISyntaxException {
        log.debug("REST request to save Departments : {}", departmentsDTO);
        if (departmentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new departments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentsDTO result = departmentsService.save(departmentsDTO);
        return ResponseEntity
            .created(new URI("/api/departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /departments/:id} : Updates an existing departments.
     *
     * @param id the id of the departmentsDTO to save.
     * @param departmentsDTO the departmentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentsDTO,
     * or with status {@code 400 (Bad Request)} if the departmentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/departments/{id}")
    public ResponseEntity<DepartmentsDTO> updateDepartments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepartmentsDTO departmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Departments : {}, {}", id, departmentsDTO);
        if (departmentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepartmentsDTO result = departmentsService.update(departmentsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /departments/:id} : Partial updates given fields of an existing departments, field will ignore if it is null
     *
     * @param id the id of the departmentsDTO to save.
     * @param departmentsDTO the departmentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentsDTO,
     * or with status {@code 400 (Bad Request)} if the departmentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the departmentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the departmentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/departments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepartmentsDTO> partialUpdateDepartments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepartmentsDTO departmentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Departments partially : {}, {}", id, departmentsDTO);
        if (departmentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepartmentsDTO> result = departmentsService.partialUpdate(departmentsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /departments} : get all the departments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departments in body.
     */
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentsDTO>> getAllDepartments(
        DepartmentsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Departments by criteria: {}", criteria);
        Page<DepartmentsDTO> page = departmentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /departments/count} : count all the departments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/departments/count")
    public ResponseEntity<Long> countDepartments(DepartmentsCriteria criteria) {
        log.debug("REST request to count Departments by criteria: {}", criteria);
        return ResponseEntity.ok().body(departmentsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /departments/:id} : get the "id" departments.
     *
     * @param id the id of the departmentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/departments/{id}")
    public ResponseEntity<DepartmentsDTO> getDepartments(@PathVariable Long id) {
        log.debug("REST request to get Departments : {}", id);
        Optional<DepartmentsDTO> departmentsDTO = departmentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentsDTO);
    }

    /**
     * {@code DELETE  /departments/:id} : delete the "id" departments.
     *
     * @param id the id of the departmentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartments(@PathVariable Long id) {
        log.debug("REST request to delete Departments : {}", id);
        departmentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
