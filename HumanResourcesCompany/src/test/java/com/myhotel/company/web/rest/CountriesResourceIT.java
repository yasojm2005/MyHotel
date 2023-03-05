package com.myhotel.company.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myhotel.company.IntegrationTest;
import com.myhotel.company.domain.Countries;
import com.myhotel.company.domain.Region;
import com.myhotel.company.repository.CountriesRepository;
import com.myhotel.company.service.criteria.CountriesCriteria;
import com.myhotel.company.service.dto.CountriesDTO;
import com.myhotel.company.service.mapper.CountriesMapper;
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
 * Integration tests for the {@link CountriesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountriesResourceIT {

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private CountriesMapper countriesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountriesMockMvc;

    private Countries countries;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Countries createEntity(EntityManager em) {
        Countries countries = new Countries().countryName(DEFAULT_COUNTRY_NAME);
        return countries;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Countries createUpdatedEntity(EntityManager em) {
        Countries countries = new Countries().countryName(UPDATED_COUNTRY_NAME);
        return countries;
    }

    @BeforeEach
    public void initTest() {
        countries = createEntity(em);
    }

    @Test
    @Transactional
    void createCountries() throws Exception {
        int databaseSizeBeforeCreate = countriesRepository.findAll().size();
        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);
        restCountriesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeCreate + 1);
        Countries testCountries = countriesList.get(countriesList.size() - 1);
        assertThat(testCountries.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void createCountriesWithExistingId() throws Exception {
        // Create the Countries with an existing ID
        countries.setId(1L);
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        int databaseSizeBeforeCreate = countriesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountriesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get all the countriesList
        restCountriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countries.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)));
    }

    @Test
    @Transactional
    void getCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get the countries
        restCountriesMockMvc
            .perform(get(ENTITY_API_URL_ID, countries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countries.getId().intValue()))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME));
    }

    @Test
    @Transactional
    void getCountriesByIdFiltering() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        Long id = countries.getId();

        defaultCountriesShouldBeFound("id.equals=" + id);
        defaultCountriesShouldNotBeFound("id.notEquals=" + id);

        defaultCountriesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountriesShouldNotBeFound("id.greaterThan=" + id);

        defaultCountriesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountriesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get all the countriesList where countryName equals to DEFAULT_COUNTRY_NAME
        defaultCountriesShouldBeFound("countryName.equals=" + DEFAULT_COUNTRY_NAME);

        // Get all the countriesList where countryName equals to UPDATED_COUNTRY_NAME
        defaultCountriesShouldNotBeFound("countryName.equals=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsInShouldWork() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get all the countriesList where countryName in DEFAULT_COUNTRY_NAME or UPDATED_COUNTRY_NAME
        defaultCountriesShouldBeFound("countryName.in=" + DEFAULT_COUNTRY_NAME + "," + UPDATED_COUNTRY_NAME);

        // Get all the countriesList where countryName equals to UPDATED_COUNTRY_NAME
        defaultCountriesShouldNotBeFound("countryName.in=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get all the countriesList where countryName is not null
        defaultCountriesShouldBeFound("countryName.specified=true");

        // Get all the countriesList where countryName is null
        defaultCountriesShouldNotBeFound("countryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameContainsSomething() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get all the countriesList where countryName contains DEFAULT_COUNTRY_NAME
        defaultCountriesShouldBeFound("countryName.contains=" + DEFAULT_COUNTRY_NAME);

        // Get all the countriesList where countryName contains UPDATED_COUNTRY_NAME
        defaultCountriesShouldNotBeFound("countryName.contains=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameNotContainsSomething() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        // Get all the countriesList where countryName does not contain DEFAULT_COUNTRY_NAME
        defaultCountriesShouldNotBeFound("countryName.doesNotContain=" + DEFAULT_COUNTRY_NAME);

        // Get all the countriesList where countryName does not contain UPDATED_COUNTRY_NAME
        defaultCountriesShouldBeFound("countryName.doesNotContain=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByRegionIsEqualToSomething() throws Exception {
        Region region;
        if (TestUtil.findAll(em, Region.class).isEmpty()) {
            countriesRepository.saveAndFlush(countries);
            region = RegionResourceIT.createEntity(em);
        } else {
            region = TestUtil.findAll(em, Region.class).get(0);
        }
        em.persist(region);
        em.flush();
        countries.setRegion(region);
        countriesRepository.saveAndFlush(countries);
        Long regionId = region.getId();

        // Get all the countriesList where region equals to regionId
        defaultCountriesShouldBeFound("regionId.equals=" + regionId);

        // Get all the countriesList where region equals to (regionId + 1)
        defaultCountriesShouldNotBeFound("regionId.equals=" + (regionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountriesShouldBeFound(String filter) throws Exception {
        restCountriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countries.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)));

        // Check, that the count call also returns 1
        restCountriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountriesShouldNotBeFound(String filter) throws Exception {
        restCountriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountries() throws Exception {
        // Get the countries
        restCountriesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();

        // Update the countries
        Countries updatedCountries = countriesRepository.findById(countries.getId()).get();
        // Disconnect from session so that the updates on updatedCountries are not directly saved in db
        em.detach(updatedCountries);
        updatedCountries.countryName(UPDATED_COUNTRY_NAME);
        CountriesDTO countriesDTO = countriesMapper.toDto(updatedCountries);

        restCountriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countriesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
        Countries testCountries = countriesList.get(countriesList.size() - 1);
        assertThat(testCountries.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCountries() throws Exception {
        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();
        countries.setId(count.incrementAndGet());

        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countriesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountries() throws Exception {
        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();
        countries.setId(count.incrementAndGet());

        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountries() throws Exception {
        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();
        countries.setId(count.incrementAndGet());

        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountriesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountriesWithPatch() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();

        // Update the countries using partial update
        Countries partialUpdatedCountries = new Countries();
        partialUpdatedCountries.setId(countries.getId());

        partialUpdatedCountries.countryName(UPDATED_COUNTRY_NAME);

        restCountriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountries.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountries))
            )
            .andExpect(status().isOk());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
        Countries testCountries = countriesList.get(countriesList.size() - 1);
        assertThat(testCountries.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCountriesWithPatch() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();

        // Update the countries using partial update
        Countries partialUpdatedCountries = new Countries();
        partialUpdatedCountries.setId(countries.getId());

        partialUpdatedCountries.countryName(UPDATED_COUNTRY_NAME);

        restCountriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountries.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountries))
            )
            .andExpect(status().isOk());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
        Countries testCountries = countriesList.get(countriesList.size() - 1);
        assertThat(testCountries.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCountries() throws Exception {
        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();
        countries.setId(count.incrementAndGet());

        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countriesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountries() throws Exception {
        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();
        countries.setId(count.incrementAndGet());

        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountries() throws Exception {
        int databaseSizeBeforeUpdate = countriesRepository.findAll().size();
        countries.setId(count.incrementAndGet());

        // Create the Countries
        CountriesDTO countriesDTO = countriesMapper.toDto(countries);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountriesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countriesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Countries in the database
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountries() throws Exception {
        // Initialize the database
        countriesRepository.saveAndFlush(countries);

        int databaseSizeBeforeDelete = countriesRepository.findAll().size();

        // Delete the countries
        restCountriesMockMvc
            .perform(delete(ENTITY_API_URL_ID, countries.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Countries> countriesList = countriesRepository.findAll();
        assertThat(countriesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
