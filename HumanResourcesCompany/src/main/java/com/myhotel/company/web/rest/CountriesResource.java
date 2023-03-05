package com.myhotel.company.web.rest;

import com.myhotel.company.repository.CountriesRepository;
import com.myhotel.company.service.CountriesQueryService;
import com.myhotel.company.service.CountriesService;
import com.myhotel.company.service.criteria.CountriesCriteria;
import com.myhotel.company.service.dto.CountriesDTO;
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
 * REST controller for managing {@link com.myhotel.company.domain.Countries}.
 */
@RestController
@RequestMapping("/api")
public class CountriesResource {

    private final Logger log = LoggerFactory.getLogger(CountriesResource.class);

    private static final String ENTITY_NAME = "countries";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountriesService countriesService;

    private final CountriesRepository countriesRepository;

    private final CountriesQueryService countriesQueryService;

    public CountriesResource(
        CountriesService countriesService,
        CountriesRepository countriesRepository,
        CountriesQueryService countriesQueryService
    ) {
        this.countriesService = countriesService;
        this.countriesRepository = countriesRepository;
        this.countriesQueryService = countriesQueryService;
    }

    /**
     * {@code POST  /countries} : Create a new countries.
     *
     * @param countriesDTO the countriesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countriesDTO, or with status {@code 400 (Bad Request)} if the countries has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/countries")
    public ResponseEntity<CountriesDTO> createCountries(@RequestBody CountriesDTO countriesDTO) throws URISyntaxException {
        log.debug("REST request to save Countries : {}", countriesDTO);
        if (countriesDTO.getId() != null) {
            throw new BadRequestAlertException("A new countries cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountriesDTO result = countriesService.save(countriesDTO);
        return ResponseEntity
            .created(new URI("/api/countries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /countries/:id} : Updates an existing countries.
     *
     * @param id the id of the countriesDTO to save.
     * @param countriesDTO the countriesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countriesDTO,
     * or with status {@code 400 (Bad Request)} if the countriesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countriesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/countries/{id}")
    public ResponseEntity<CountriesDTO> updateCountries(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CountriesDTO countriesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Countries : {}, {}", id, countriesDTO);
        if (countriesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countriesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountriesDTO result = countriesService.update(countriesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countriesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /countries/:id} : Partial updates given fields of an existing countries, field will ignore if it is null
     *
     * @param id the id of the countriesDTO to save.
     * @param countriesDTO the countriesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countriesDTO,
     * or with status {@code 400 (Bad Request)} if the countriesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the countriesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the countriesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/countries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountriesDTO> partialUpdateCountries(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CountriesDTO countriesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Countries partially : {}, {}", id, countriesDTO);
        if (countriesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countriesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountriesDTO> result = countriesService.partialUpdate(countriesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countriesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /countries} : get all the countries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countries in body.
     */
    @GetMapping("/countries")
    public ResponseEntity<List<CountriesDTO>> getAllCountries(
        CountriesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Countries by criteria: {}", criteria);
        Page<CountriesDTO> page = countriesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /countries/count} : count all the countries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/countries/count")
    public ResponseEntity<Long> countCountries(CountriesCriteria criteria) {
        log.debug("REST request to count Countries by criteria: {}", criteria);
        return ResponseEntity.ok().body(countriesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /countries/:id} : get the "id" countries.
     *
     * @param id the id of the countriesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countriesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/countries/{id}")
    public ResponseEntity<CountriesDTO> getCountries(@PathVariable Long id) {
        log.debug("REST request to get Countries : {}", id);
        Optional<CountriesDTO> countriesDTO = countriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countriesDTO);
    }

    /**
     * {@code DELETE  /countries/:id} : delete the "id" countries.
     *
     * @param id the id of the countriesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Void> deleteCountries(@PathVariable Long id) {
        log.debug("REST request to delete Countries : {}", id);
        countriesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
