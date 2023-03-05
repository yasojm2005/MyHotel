package com.myhotel.company.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myhotel.company.IntegrationTest;
import com.myhotel.company.domain.Departments;
import com.myhotel.company.domain.Locations;
import com.myhotel.company.repository.DepartmentsRepository;
import com.myhotel.company.service.criteria.DepartmentsCriteria;
import com.myhotel.company.service.dto.DepartmentsDTO;
import com.myhotel.company.service.mapper.DepartmentsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DepartmentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartmentsResourceIT {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DEPARMENT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Autowired
    private DepartmentsMapper departmentsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartmentsMockMvc;

    private Departments departments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departments createEntity(EntityManager em) {
        Departments departments = new Departments().departmentName(DEFAULT_DEPARTMENT_NAME).deparmentType(DEFAULT_DEPARMENT_TYPE);
        return departments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departments createUpdatedEntity(EntityManager em) {
        Departments departments = new Departments().departmentName(UPDATED_DEPARTMENT_NAME).deparmentType(UPDATED_DEPARMENT_TYPE);
        return departments;
    }

    @BeforeEach
    public void initTest() {
        departments = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartments() throws Exception {
        int databaseSizeBeforeCreate = departmentsRepository.findAll().size();
        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);
        restDepartmentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeCreate + 1);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
        assertThat(testDepartments.getDeparmentType()).isEqualTo(DEFAULT_DEPARMENT_TYPE);
    }

    @Test
    @Transactional
    void createDepartmentsWithExistingId() throws Exception {
        // Create the Departments with an existing ID
        departments.setId(1L);
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        int databaseSizeBeforeCreate = departmentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departments.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].deparmentType").value(hasItem(DEFAULT_DEPARMENT_TYPE)));
    }

    @Test
    @Transactional
    void getDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get the departments
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL_ID, departments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departments.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME))
            .andExpect(jsonPath("$.deparmentType").value(DEFAULT_DEPARMENT_TYPE));
    }

    @Test
    @Transactional
    void getDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        Long id = departments.getId();

        defaultDepartmentsShouldBeFound("id.equals=" + id);
        defaultDepartmentsShouldNotBeFound("id.notEquals=" + id);

        defaultDepartmentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartmentsShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartmentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartmentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName equals to DEFAULT_DEPARTMENT_NAME
        defaultDepartmentsShouldBeFound("departmentName.equals=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departmentsList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldNotBeFound("departmentName.equals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName in DEFAULT_DEPARTMENT_NAME or UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldBeFound("departmentName.in=" + DEFAULT_DEPARTMENT_NAME + "," + UPDATED_DEPARTMENT_NAME);

        // Get all the departmentsList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldNotBeFound("departmentName.in=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName is not null
        defaultDepartmentsShouldBeFound("departmentName.specified=true");

        // Get all the departmentsList where departmentName is null
        defaultDepartmentsShouldNotBeFound("departmentName.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameContainsSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName contains DEFAULT_DEPARTMENT_NAME
        defaultDepartmentsShouldBeFound("departmentName.contains=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departmentsList where departmentName contains UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldNotBeFound("departmentName.contains=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDepartmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where departmentName does not contain DEFAULT_DEPARTMENT_NAME
        defaultDepartmentsShouldNotBeFound("departmentName.doesNotContain=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departmentsList where departmentName does not contain UPDATED_DEPARTMENT_NAME
        defaultDepartmentsShouldBeFound("departmentName.doesNotContain=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDeparmentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where deparmentType equals to DEFAULT_DEPARMENT_TYPE
        defaultDepartmentsShouldBeFound("deparmentType.equals=" + DEFAULT_DEPARMENT_TYPE);

        // Get all the departmentsList where deparmentType equals to UPDATED_DEPARMENT_TYPE
        defaultDepartmentsShouldNotBeFound("deparmentType.equals=" + UPDATED_DEPARMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDeparmentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where deparmentType in DEFAULT_DEPARMENT_TYPE or UPDATED_DEPARMENT_TYPE
        defaultDepartmentsShouldBeFound("deparmentType.in=" + DEFAULT_DEPARMENT_TYPE + "," + UPDATED_DEPARMENT_TYPE);

        // Get all the departmentsList where deparmentType equals to UPDATED_DEPARMENT_TYPE
        defaultDepartmentsShouldNotBeFound("deparmentType.in=" + UPDATED_DEPARMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDeparmentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where deparmentType is not null
        defaultDepartmentsShouldBeFound("deparmentType.specified=true");

        // Get all the departmentsList where deparmentType is null
        defaultDepartmentsShouldNotBeFound("deparmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartmentsByDeparmentTypeContainsSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where deparmentType contains DEFAULT_DEPARMENT_TYPE
        defaultDepartmentsShouldBeFound("deparmentType.contains=" + DEFAULT_DEPARMENT_TYPE);

        // Get all the departmentsList where deparmentType contains UPDATED_DEPARMENT_TYPE
        defaultDepartmentsShouldNotBeFound("deparmentType.contains=" + UPDATED_DEPARMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByDeparmentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        // Get all the departmentsList where deparmentType does not contain DEFAULT_DEPARMENT_TYPE
        defaultDepartmentsShouldNotBeFound("deparmentType.doesNotContain=" + DEFAULT_DEPARMENT_TYPE);

        // Get all the departmentsList where deparmentType does not contain UPDATED_DEPARMENT_TYPE
        defaultDepartmentsShouldBeFound("deparmentType.doesNotContain=" + UPDATED_DEPARMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDepartmentsByLocationsIsEqualToSomething() throws Exception {
        Locations locations;
        if (TestUtil.findAll(em, Locations.class).isEmpty()) {
            departmentsRepository.saveAndFlush(departments);
            locations = LocationsResourceIT.createEntity(em);
        } else {
            locations = TestUtil.findAll(em, Locations.class).get(0);
        }
        em.persist(locations);
        em.flush();
        departments.setLocations(locations);
        departmentsRepository.saveAndFlush(departments);
        Long locationsId = locations.getId();

        // Get all the departmentsList where locations equals to locationsId
        defaultDepartmentsShouldBeFound("locationsId.equals=" + locationsId);

        // Get all the departmentsList where locations equals to (locationsId + 1)
        defaultDepartmentsShouldNotBeFound("locationsId.equals=" + (locationsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentsShouldBeFound(String filter) throws Exception {
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departments.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].deparmentType").value(hasItem(DEFAULT_DEPARMENT_TYPE)));

        // Check, that the count call also returns 1
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentsShouldNotBeFound(String filter) throws Exception {
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartments() throws Exception {
        // Get the departments
        restDepartmentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments
        Departments updatedDepartments = departmentsRepository.findById(departments.getId()).get();
        // Disconnect from session so that the updates on updatedDepartments are not directly saved in db
        em.detach(updatedDepartments);
        updatedDepartments.departmentName(UPDATED_DEPARTMENT_NAME).deparmentType(UPDATED_DEPARMENT_TYPE);
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(updatedDepartments);

        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testDepartments.getDeparmentType()).isEqualTo(UPDATED_DEPARMENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departmentsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartmentsWithPatch() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments using partial update
        Departments partialUpdatedDepartments = new Departments();
        partialUpdatedDepartments.setId(departments.getId());

        partialUpdatedDepartments.deparmentType(UPDATED_DEPARMENT_TYPE);

        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartments.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartments))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
        assertThat(testDepartments.getDeparmentType()).isEqualTo(UPDATED_DEPARMENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDepartmentsWithPatch() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();

        // Update the departments using partial update
        Departments partialUpdatedDepartments = new Departments();
        partialUpdatedDepartments.setId(departments.getId());

        partialUpdatedDepartments.departmentName(UPDATED_DEPARTMENT_NAME).deparmentType(UPDATED_DEPARMENT_TYPE);

        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartments.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartments))
            )
            .andExpect(status().isOk());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
        Departments testDepartments = departmentsList.get(departmentsList.size() - 1);
        assertThat(testDepartments.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testDepartments.getDeparmentType()).isEqualTo(UPDATED_DEPARMENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departmentsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartments() throws Exception {
        int databaseSizeBeforeUpdate = departmentsRepository.findAll().size();
        departments.setId(count.incrementAndGet());

        // Create the Departments
        DepartmentsDTO departmentsDTO = departmentsMapper.toDto(departments);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartmentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departmentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departments in the database
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartments() throws Exception {
        // Initialize the database
        departmentsRepository.saveAndFlush(departments);

        int databaseSizeBeforeDelete = departmentsRepository.findAll().size();

        // Delete the departments
        restDepartmentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, departments.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departments> departmentsList = departmentsRepository.findAll();
        assertThat(departmentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
