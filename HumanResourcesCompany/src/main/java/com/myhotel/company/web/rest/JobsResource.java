package com.myhotel.company.web.rest;

import com.myhotel.company.repository.JobsRepository;
import com.myhotel.company.service.JobsQueryService;
import com.myhotel.company.service.JobsService;
import com.myhotel.company.service.criteria.JobsCriteria;
import com.myhotel.company.service.dto.JobsDTO;
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
 * REST controller for managing {@link com.myhotel.company.domain.Jobs}.
 */
@RestController
@RequestMapping("/api")
public class JobsResource {

    private final Logger log = LoggerFactory.getLogger(JobsResource.class);

    private final JobsService jobsService;

    private final JobsRepository jobsRepository;

    private final JobsQueryService jobsQueryService;

    public JobsResource(JobsService jobsService, JobsRepository jobsRepository, JobsQueryService jobsQueryService) {
        this.jobsService = jobsService;
        this.jobsRepository = jobsRepository;
        this.jobsQueryService = jobsQueryService;
    }

    /**
     * {@code GET  /jobs} : get all the jobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobs in body.
     */
    @GetMapping("/jobs")
    public ResponseEntity<List<JobsDTO>> getAllJobs(
        JobsCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Jobs by criteria: {}", criteria);
        Page<JobsDTO> page = jobsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /jobs/count} : count all the jobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/jobs/count")
    public ResponseEntity<Long> countJobs(JobsCriteria criteria) {
        log.debug("REST request to count Jobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /jobs/:id} : get the "id" jobs.
     *
     * @param id the id of the jobsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobsDTO> getJobs(@PathVariable Long id) {
        log.debug("REST request to get Jobs : {}", id);
        Optional<JobsDTO> jobsDTO = jobsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobsDTO);
    }
}
