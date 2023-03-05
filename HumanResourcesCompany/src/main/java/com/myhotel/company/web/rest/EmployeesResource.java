package com.myhotel.company.web.rest;

import com.myhotel.company.repository.EmployeesRepository;
import com.myhotel.company.service.EmployeesQueryService;
import com.myhotel.company.service.EmployeesService;
import com.myhotel.company.service.criteria.EmployeesCriteria;
import com.myhotel.company.service.dto.EmployeesDTO;
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
 * REST controller for managing {@link com.myhotel.company.domain.Employees}.
 */
@RestController
@RequestMapping("/api")
public class EmployeesResource {

    private final Logger log = LoggerFactory.getLogger(EmployeesResource.class);

    private static final String ENTITY_NAME = "employees";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeesService employeesService;

    private final EmployeesRepository employeesRepository;

    private final EmployeesQueryService employeesQueryService;

    public EmployeesResource(
        EmployeesService employeesService,
        EmployeesRepository employeesRepository,
        EmployeesQueryService employeesQueryService
    ) {
        this.employeesService = employeesService;
        this.employeesRepository = employeesRepository;
        this.employeesQueryService = employeesQueryService;
    }

    /**
     * {@code POST  /employees} : Create a new employees.
     *
     * @param employeesDTO the employeesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeesDTO, or with status {@code 400 (Bad Request)} if the employees has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employees")
    public ResponseEntity<EmployeesDTO> createEmployees(@RequestBody EmployeesDTO employeesDTO) throws URISyntaxException {
        log.debug("REST request to save Employees : {}", employeesDTO);
        if (employeesDTO.getId() != null) {
            throw new BadRequestAlertException("A new employees cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeesDTO result = employeesService.save(employeesDTO);
        return ResponseEntity
            .created(new URI("/api/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employees/:id} : Updates an existing employees.
     *
     * @param id the id of the employeesDTO to save.
     * @param employeesDTO the employeesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeesDTO,
     * or with status {@code 400 (Bad Request)} if the employeesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeesDTO> updateEmployees(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeesDTO employeesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Employees : {}, {}", id, employeesDTO);
        if (employeesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmployeesDTO result = employeesService.update(employeesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employees/:id} : Partial updates given fields of an existing employees, field will ignore if it is null
     *
     * @param id the id of the employeesDTO to save.
     * @param employeesDTO the employeesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeesDTO,
     * or with status {@code 400 (Bad Request)} if the employeesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeesDTO> partialUpdateEmployees(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmployeesDTO employeesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Employees partially : {}, {}", id, employeesDTO);
        if (employeesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeesDTO> result = employeesService.partialUpdate(employeesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body.
     */
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeesDTO>> getAllEmployees(
        EmployeesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Employees by criteria: {}", criteria);
        Page<EmployeesDTO> page = employeesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employees/count} : count all the employees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employees/count")
    public ResponseEntity<Long> countEmployees(EmployeesCriteria criteria) {
        log.debug("REST request to count Employees by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employees/:id} : get the "id" employees.
     *
     * @param id the id of the employeesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeesDTO> getEmployees(@PathVariable Long id) {
        log.debug("REST request to get Employees : {}", id);
        Optional<EmployeesDTO> employeesDTO = employeesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeesDTO);
    }

    /**
     * {@code DELETE  /employees/:id} : delete the "id" employees.
     *
     * @param id the id of the employeesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployees(@PathVariable Long id) {
        log.debug("REST request to delete Employees : {}", id);
        employeesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


}
