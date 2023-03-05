package com.myhotel.truck.web.rest;

import static com.myhotel.truck.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myhotel.truck.IntegrationTest;
import com.myhotel.truck.domain.Maintenance;
import com.myhotel.truck.domain.Vehicle;
import com.myhotel.truck.domain.enumeration.MaintainanceType;
import com.myhotel.truck.repository.MaintenanceRepository;
import com.myhotel.truck.service.criteria.MaintenanceCriteria;
import com.myhotel.truck.service.dto.MaintenanceDTO;
import com.myhotel.truck.service.mapper.MaintenanceMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MaintenanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaintenanceResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final MaintainanceType DEFAULT_TYPE = MaintainanceType.CORRECTIVE;
    private static final MaintainanceType UPDATED_TYPE = MaintainanceType.PREVENTIVE;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/maintenances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private MaintenanceMapper maintenanceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaintenanceMockMvc;

    private Maintenance maintenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintenance createEntity(EntityManager em) {
        Maintenance maintenance = new Maintenance()
            .date(DEFAULT_DATE)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE);
        return maintenance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maintenance createUpdatedEntity(EntityManager em) {
        Maintenance maintenance = new Maintenance()
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE);
        return maintenance;
    }

    @BeforeEach
    public void initTest() {
        maintenance = createEntity(em);
    }

    @Test
    @Transactional
    void createMaintenance() throws Exception {
        int databaseSizeBeforeCreate = maintenanceRepository.findAll().size();
        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);
        restMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMaintenance.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMaintenance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMaintenance.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createMaintenanceWithExistingId() throws Exception {
        // Create the Maintenance with an existing ID
        maintenance.setId(1L);
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        int databaseSizeBeforeCreate = maintenanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintenanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaintenances() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList
        restMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    @Transactional
    void getMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get the maintenance
        restMaintenanceMockMvc
            .perform(get(ENTITY_API_URL_ID, maintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maintenance.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getMaintenancesByIdFiltering() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        Long id = maintenance.getId();

        defaultMaintenanceShouldBeFound("id.equals=" + id);
        defaultMaintenanceShouldNotBeFound("id.notEquals=" + id);

        defaultMaintenanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMaintenanceShouldNotBeFound("id.greaterThan=" + id);

        defaultMaintenanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMaintenanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where date equals to DEFAULT_DATE
        defaultMaintenanceShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the maintenanceList where date equals to UPDATED_DATE
        defaultMaintenanceShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where date in DEFAULT_DATE or UPDATED_DATE
        defaultMaintenanceShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the maintenanceList where date equals to UPDATED_DATE
        defaultMaintenanceShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where date is not null
        defaultMaintenanceShouldBeFound("date.specified=true");

        // Get all the maintenanceList where date is null
        defaultMaintenanceShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenancesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where date is greater than or equal to DEFAULT_DATE
        defaultMaintenanceShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the maintenanceList where date is greater than or equal to UPDATED_DATE
        defaultMaintenanceShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where date is less than or equal to DEFAULT_DATE
        defaultMaintenanceShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the maintenanceList where date is less than or equal to SMALLER_DATE
        defaultMaintenanceShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where date is less than DEFAULT_DATE
        defaultMaintenanceShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the maintenanceList where date is less than UPDATED_DATE
        defaultMaintenanceShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where date is greater than DEFAULT_DATE
        defaultMaintenanceShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the maintenanceList where date is greater than SMALLER_DATE
        defaultMaintenanceShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where type equals to DEFAULT_TYPE
        defaultMaintenanceShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the maintenanceList where type equals to UPDATED_TYPE
        defaultMaintenanceShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultMaintenanceShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the maintenanceList where type equals to UPDATED_TYPE
        defaultMaintenanceShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where type is not null
        defaultMaintenanceShouldBeFound("type.specified=true");

        // Get all the maintenanceList where type is null
        defaultMaintenanceShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenancesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description equals to DEFAULT_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the maintenanceList where description equals to UPDATED_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the maintenanceList where description equals to UPDATED_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description is not null
        defaultMaintenanceShouldBeFound("description.specified=true");

        // Get all the maintenanceList where description is null
        defaultMaintenanceShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenancesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description contains DEFAULT_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the maintenanceList where description contains UPDATED_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaintenancesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where description does not contain DEFAULT_DESCRIPTION
        defaultMaintenanceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the maintenanceList where description does not contain UPDATED_DESCRIPTION
        defaultMaintenanceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMaintenancesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where price equals to DEFAULT_PRICE
        defaultMaintenanceShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the maintenanceList where price equals to UPDATED_PRICE
        defaultMaintenanceShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultMaintenanceShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the maintenanceList where price equals to UPDATED_PRICE
        defaultMaintenanceShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where price is not null
        defaultMaintenanceShouldBeFound("price.specified=true");

        // Get all the maintenanceList where price is null
        defaultMaintenanceShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenancesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where price is greater than or equal to DEFAULT_PRICE
        defaultMaintenanceShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the maintenanceList where price is greater than or equal to UPDATED_PRICE
        defaultMaintenanceShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where price is less than or equal to DEFAULT_PRICE
        defaultMaintenanceShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the maintenanceList where price is less than or equal to SMALLER_PRICE
        defaultMaintenanceShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where price is less than DEFAULT_PRICE
        defaultMaintenanceShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the maintenanceList where price is less than UPDATED_PRICE
        defaultMaintenanceShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList where price is greater than DEFAULT_PRICE
        defaultMaintenanceShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the maintenanceList where price is greater than SMALLER_PRICE
        defaultMaintenanceShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllMaintenancesByVehicleIsEqualToSomething() throws Exception {
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            maintenanceRepository.saveAndFlush(maintenance);
            vehicle = VehicleResourceIT.createEntity(em);
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        em.persist(vehicle);
        em.flush();
        maintenance.setVehicle(vehicle);
        maintenanceRepository.saveAndFlush(maintenance);
        Long vehicleId = vehicle.getId();

        // Get all the maintenanceList where vehicle equals to vehicleId
        defaultMaintenanceShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the maintenanceList where vehicle equals to (vehicleId + 1)
        defaultMaintenanceShouldNotBeFound("vehicleId.equals=" + (vehicleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaintenanceShouldBeFound(String filter) throws Exception {
        restMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));

        // Check, that the count call also returns 1
        restMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaintenanceShouldNotBeFound(String filter) throws Exception {
        restMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaintenance() throws Exception {
        // Get the maintenance
        restMaintenanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Update the maintenance
        Maintenance updatedMaintenance = maintenanceRepository.findById(maintenance.getId()).get();
        // Disconnect from session so that the updates on updatedMaintenance are not directly saved in db
        em.detach(updatedMaintenance);
        updatedMaintenance.date(UPDATED_DATE).type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(updatedMaintenance);

        restMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintenanceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMaintenance.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMaintenance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaintenance.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintenanceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaintenanceWithPatch() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Update the maintenance using partial update
        Maintenance partialUpdatedMaintenance = new Maintenance();
        partialUpdatedMaintenance.setId(maintenance.getId());

        partialUpdatedMaintenance.date(UPDATED_DATE).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintenance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMaintenance.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMaintenance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaintenance.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateMaintenanceWithPatch() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Update the maintenance using partial update
        Maintenance partialUpdatedMaintenance = new Maintenance();
        partialUpdatedMaintenance.setId(maintenance.getId());

        partialUpdatedMaintenance.date(UPDATED_DATE).type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).price(UPDATED_PRICE);

        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintenance.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMaintenance.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMaintenance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaintenance.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maintenanceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();
        maintenance.setId(count.incrementAndGet());

        // Create the Maintenance
        MaintenanceDTO maintenanceDTO = maintenanceMapper.toDto(maintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(maintenanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        int databaseSizeBeforeDelete = maintenanceRepository.findAll().size();

        // Delete the maintenance
        restMaintenanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, maintenance.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
