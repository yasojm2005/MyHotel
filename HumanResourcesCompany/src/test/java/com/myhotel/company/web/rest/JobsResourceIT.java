package com.myhotel.company.web.rest;

import static com.myhotel.company.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myhotel.company.IntegrationTest;
import com.myhotel.company.domain.Employees;
import com.myhotel.company.domain.Jobs;
import com.myhotel.company.repository.JobsRepository;
import com.myhotel.company.service.JobsService;
import com.myhotel.company.service.criteria.JobsCriteria;
import com.myhotel.company.service.dto.JobsDTO;
import com.myhotel.company.service.mapper.JobsMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JobsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobsResourceIT {

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MIN_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_MIN_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_MIN_SALARY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MAX_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_MAX_SALARY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobsRepository jobsRepository;

    @Mock
    private JobsRepository jobsRepositoryMock;

    @Autowired
    private JobsMapper jobsMapper;

    @Mock
    private JobsService jobsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobsMockMvc;

    private Jobs jobs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jobs createEntity(EntityManager em) {
        Jobs jobs = new Jobs().jobTitle(DEFAULT_JOB_TITLE).minSalary(DEFAULT_MIN_SALARY).maxSalary(DEFAULT_MAX_SALARY);
        return jobs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jobs createUpdatedEntity(EntityManager em) {
        Jobs jobs = new Jobs().jobTitle(UPDATED_JOB_TITLE).minSalary(UPDATED_MIN_SALARY).maxSalary(UPDATED_MAX_SALARY);
        return jobs;
    }

    @BeforeEach
    public void initTest() {
        jobs = createEntity(em);
    }

    @Test
    @Transactional
    void getAllJobs() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList
        restJobsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobs.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(sameNumber(DEFAULT_MIN_SALARY))))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(sameNumber(DEFAULT_MAX_SALARY))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(jobsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getJobs() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get the jobs
        restJobsMockMvc
            .perform(get(ENTITY_API_URL_ID, jobs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobs.getId().intValue()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.minSalary").value(sameNumber(DEFAULT_MIN_SALARY)))
            .andExpect(jsonPath("$.maxSalary").value(sameNumber(DEFAULT_MAX_SALARY)));
    }

    @Test
    @Transactional
    void getJobsByIdFiltering() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        Long id = jobs.getId();

        defaultJobsShouldBeFound("id.equals=" + id);
        defaultJobsShouldNotBeFound("id.notEquals=" + id);

        defaultJobsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultJobsShouldNotBeFound("id.greaterThan=" + id);

        defaultJobsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultJobsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where jobTitle equals to DEFAULT_JOB_TITLE
        defaultJobsShouldBeFound("jobTitle.equals=" + DEFAULT_JOB_TITLE);

        // Get all the jobsList where jobTitle equals to UPDATED_JOB_TITLE
        defaultJobsShouldNotBeFound("jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where jobTitle in DEFAULT_JOB_TITLE or UPDATED_JOB_TITLE
        defaultJobsShouldBeFound("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE);

        // Get all the jobsList where jobTitle equals to UPDATED_JOB_TITLE
        defaultJobsShouldNotBeFound("jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where jobTitle is not null
        defaultJobsShouldBeFound("jobTitle.specified=true");

        // Get all the jobsList where jobTitle is null
        defaultJobsShouldNotBeFound("jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where jobTitle contains DEFAULT_JOB_TITLE
        defaultJobsShouldBeFound("jobTitle.contains=" + DEFAULT_JOB_TITLE);

        // Get all the jobsList where jobTitle contains UPDATED_JOB_TITLE
        defaultJobsShouldNotBeFound("jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllJobsByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where jobTitle does not contain DEFAULT_JOB_TITLE
        defaultJobsShouldNotBeFound("jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);

        // Get all the jobsList where jobTitle does not contain UPDATED_JOB_TITLE
        defaultJobsShouldBeFound("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where minSalary equals to DEFAULT_MIN_SALARY
        defaultJobsShouldBeFound("minSalary.equals=" + DEFAULT_MIN_SALARY);

        // Get all the jobsList where minSalary equals to UPDATED_MIN_SALARY
        defaultJobsShouldNotBeFound("minSalary.equals=" + UPDATED_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsInShouldWork() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where minSalary in DEFAULT_MIN_SALARY or UPDATED_MIN_SALARY
        defaultJobsShouldBeFound("minSalary.in=" + DEFAULT_MIN_SALARY + "," + UPDATED_MIN_SALARY);

        // Get all the jobsList where minSalary equals to UPDATED_MIN_SALARY
        defaultJobsShouldNotBeFound("minSalary.in=" + UPDATED_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where minSalary is not null
        defaultJobsShouldBeFound("minSalary.specified=true");

        // Get all the jobsList where minSalary is null
        defaultJobsShouldNotBeFound("minSalary.specified=false");
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where minSalary is greater than or equal to DEFAULT_MIN_SALARY
        defaultJobsShouldBeFound("minSalary.greaterThanOrEqual=" + DEFAULT_MIN_SALARY);

        // Get all the jobsList where minSalary is greater than or equal to UPDATED_MIN_SALARY
        defaultJobsShouldNotBeFound("minSalary.greaterThanOrEqual=" + UPDATED_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where minSalary is less than or equal to DEFAULT_MIN_SALARY
        defaultJobsShouldBeFound("minSalary.lessThanOrEqual=" + DEFAULT_MIN_SALARY);

        // Get all the jobsList where minSalary is less than or equal to SMALLER_MIN_SALARY
        defaultJobsShouldNotBeFound("minSalary.lessThanOrEqual=" + SMALLER_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where minSalary is less than DEFAULT_MIN_SALARY
        defaultJobsShouldNotBeFound("minSalary.lessThan=" + DEFAULT_MIN_SALARY);

        // Get all the jobsList where minSalary is less than UPDATED_MIN_SALARY
        defaultJobsShouldBeFound("minSalary.lessThan=" + UPDATED_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMinSalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where minSalary is greater than DEFAULT_MIN_SALARY
        defaultJobsShouldNotBeFound("minSalary.greaterThan=" + DEFAULT_MIN_SALARY);

        // Get all the jobsList where minSalary is greater than SMALLER_MIN_SALARY
        defaultJobsShouldBeFound("minSalary.greaterThan=" + SMALLER_MIN_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where maxSalary equals to DEFAULT_MAX_SALARY
        defaultJobsShouldBeFound("maxSalary.equals=" + DEFAULT_MAX_SALARY);

        // Get all the jobsList where maxSalary equals to UPDATED_MAX_SALARY
        defaultJobsShouldNotBeFound("maxSalary.equals=" + UPDATED_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsInShouldWork() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where maxSalary in DEFAULT_MAX_SALARY or UPDATED_MAX_SALARY
        defaultJobsShouldBeFound("maxSalary.in=" + DEFAULT_MAX_SALARY + "," + UPDATED_MAX_SALARY);

        // Get all the jobsList where maxSalary equals to UPDATED_MAX_SALARY
        defaultJobsShouldNotBeFound("maxSalary.in=" + UPDATED_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where maxSalary is not null
        defaultJobsShouldBeFound("maxSalary.specified=true");

        // Get all the jobsList where maxSalary is null
        defaultJobsShouldNotBeFound("maxSalary.specified=false");
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where maxSalary is greater than or equal to DEFAULT_MAX_SALARY
        defaultJobsShouldBeFound("maxSalary.greaterThanOrEqual=" + DEFAULT_MAX_SALARY);

        // Get all the jobsList where maxSalary is greater than or equal to UPDATED_MAX_SALARY
        defaultJobsShouldNotBeFound("maxSalary.greaterThanOrEqual=" + UPDATED_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where maxSalary is less than or equal to DEFAULT_MAX_SALARY
        defaultJobsShouldBeFound("maxSalary.lessThanOrEqual=" + DEFAULT_MAX_SALARY);

        // Get all the jobsList where maxSalary is less than or equal to SMALLER_MAX_SALARY
        defaultJobsShouldNotBeFound("maxSalary.lessThanOrEqual=" + SMALLER_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where maxSalary is less than DEFAULT_MAX_SALARY
        defaultJobsShouldNotBeFound("maxSalary.lessThan=" + DEFAULT_MAX_SALARY);

        // Get all the jobsList where maxSalary is less than UPDATED_MAX_SALARY
        defaultJobsShouldBeFound("maxSalary.lessThan=" + UPDATED_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByMaxSalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        jobsRepository.saveAndFlush(jobs);

        // Get all the jobsList where maxSalary is greater than DEFAULT_MAX_SALARY
        defaultJobsShouldNotBeFound("maxSalary.greaterThan=" + DEFAULT_MAX_SALARY);

        // Get all the jobsList where maxSalary is greater than SMALLER_MAX_SALARY
        defaultJobsShouldBeFound("maxSalary.greaterThan=" + SMALLER_MAX_SALARY);
    }

    @Test
    @Transactional
    void getAllJobsByJobHistoryIsEqualToSomething() throws Exception {
        Employees jobHistory;
        if (TestUtil.findAll(em, Employees.class).isEmpty()) {
            jobsRepository.saveAndFlush(jobs);
            jobHistory = EmployeesResourceIT.createEntity(em);
        } else {
            jobHistory = TestUtil.findAll(em, Employees.class).get(0);
        }
        em.persist(jobHistory);
        em.flush();
        jobs.addJobHistory(jobHistory);
        jobsRepository.saveAndFlush(jobs);
        Long jobHistoryId = jobHistory.getId();

        // Get all the jobsList where jobHistory equals to jobHistoryId
        defaultJobsShouldBeFound("jobHistoryId.equals=" + jobHistoryId);

        // Get all the jobsList where jobHistory equals to (jobHistoryId + 1)
        defaultJobsShouldNotBeFound("jobHistoryId.equals=" + (jobHistoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultJobsShouldBeFound(String filter) throws Exception {
        restJobsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobs.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(sameNumber(DEFAULT_MIN_SALARY))))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(sameNumber(DEFAULT_MAX_SALARY))));

        // Check, that the count call also returns 1
        restJobsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultJobsShouldNotBeFound(String filter) throws Exception {
        restJobsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restJobsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingJobs() throws Exception {
        // Get the jobs
        restJobsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
