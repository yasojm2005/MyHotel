package com.myhotel.company.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myhotel.company.IntegrationTest;
import com.myhotel.company.domain.Countries;
import com.myhotel.company.domain.Locations;
import com.myhotel.company.repository.LocationsRepository;
import com.myhotel.company.service.criteria.LocationsCriteria;
import com.myhotel.company.service.dto.LocationsDTO;
import com.myhotel.company.service.mapper.LocationsMapper;
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
 * Integration tests for the {@link LocationsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationsResourceIT {

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationsRepository locationsRepository;

    @Autowired
    private LocationsMapper locationsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationsMockMvc;

    private Locations locations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locations createEntity(EntityManager em) {
        Locations locations = new Locations()
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .province(DEFAULT_PROVINCE);
        return locations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locations createUpdatedEntity(EntityManager em) {
        Locations locations = new Locations()
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE);
        return locations;
    }

    @BeforeEach
    public void initTest() {
        locations = createEntity(em);
    }

    @Test
    @Transactional
    void createLocations() throws Exception {
        int databaseSizeBeforeCreate = locationsRepository.findAll().size();
        // Create the Locations
        LocationsDTO locationsDTO = locationsMapper.toDto(locations);
        restLocationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeCreate + 1);
        Locations testLocations = locationsList.get(locationsList.size() - 1);
        assertThat(testLocations.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testLocations.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testLocations.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testLocations.getProvince()).isEqualTo(DEFAULT_PROVINCE);
    }

    @Test
    @Transactional
    void createLocationsWithExistingId() throws Exception {
        // Create the Locations with an existing ID
        locations.setId(1L);
        LocationsDTO locationsDTO = locationsMapper.toDto(locations);

        int databaseSizeBeforeCreate = locationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList
        restLocationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locations.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)));
    }

    @Test
    @Transactional
    void getLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get the locations
        restLocationsMockMvc
            .perform(get(ENTITY_API_URL_ID, locations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locations.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE));
    }

    @Test
    @Transactional
    void getLocationsByIdFiltering() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        Long id = locations.getId();

        defaultLocationsShouldBeFound("id.equals=" + id);
        defaultLocationsShouldNotBeFound("id.notEquals=" + id);

        defaultLocationsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocationsShouldNotBeFound("id.greaterThan=" + id);

        defaultLocationsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocationsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultLocationsShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the locationsList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultLocationsShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultLocationsShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the locationsList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultLocationsShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where streetAddress is not null
        defaultLocationsShouldBeFound("streetAddress.specified=true");

        // Get all the locationsList where streetAddress is null
        defaultLocationsShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultLocationsShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the locationsList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultLocationsShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultLocationsShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the locationsList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultLocationsShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultLocationsShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the locationsList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationsShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultLocationsShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the locationsList where postalCode equals to UPDATED_POSTAL_CODE
        defaultLocationsShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where postalCode is not null
        defaultLocationsShouldBeFound("postalCode.specified=true");

        // Get all the locationsList where postalCode is null
        defaultLocationsShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where postalCode contains DEFAULT_POSTAL_CODE
        defaultLocationsShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the locationsList where postalCode contains UPDATED_POSTAL_CODE
        defaultLocationsShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultLocationsShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the locationsList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultLocationsShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where city equals to DEFAULT_CITY
        defaultLocationsShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the locationsList where city equals to UPDATED_CITY
        defaultLocationsShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where city in DEFAULT_CITY or UPDATED_CITY
        defaultLocationsShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the locationsList where city equals to UPDATED_CITY
        defaultLocationsShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where city is not null
        defaultLocationsShouldBeFound("city.specified=true");

        // Get all the locationsList where city is null
        defaultLocationsShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByCityContainsSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where city contains DEFAULT_CITY
        defaultLocationsShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the locationsList where city contains UPDATED_CITY
        defaultLocationsShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where city does not contain DEFAULT_CITY
        defaultLocationsShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the locationsList where city does not contain UPDATED_CITY
        defaultLocationsShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLocationsByProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where province equals to DEFAULT_PROVINCE
        defaultLocationsShouldBeFound("province.equals=" + DEFAULT_PROVINCE);

        // Get all the locationsList where province equals to UPDATED_PROVINCE
        defaultLocationsShouldNotBeFound("province.equals=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllLocationsByProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where province in DEFAULT_PROVINCE or UPDATED_PROVINCE
        defaultLocationsShouldBeFound("province.in=" + DEFAULT_PROVINCE + "," + UPDATED_PROVINCE);

        // Get all the locationsList where province equals to UPDATED_PROVINCE
        defaultLocationsShouldNotBeFound("province.in=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllLocationsByProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where province is not null
        defaultLocationsShouldBeFound("province.specified=true");

        // Get all the locationsList where province is null
        defaultLocationsShouldNotBeFound("province.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByProvinceContainsSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where province contains DEFAULT_PROVINCE
        defaultLocationsShouldBeFound("province.contains=" + DEFAULT_PROVINCE);

        // Get all the locationsList where province contains UPDATED_PROVINCE
        defaultLocationsShouldNotBeFound("province.contains=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllLocationsByProvinceNotContainsSomething() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        // Get all the locationsList where province does not contain DEFAULT_PROVINCE
        defaultLocationsShouldNotBeFound("province.doesNotContain=" + DEFAULT_PROVINCE);

        // Get all the locationsList where province does not contain UPDATED_PROVINCE
        defaultLocationsShouldBeFound("province.doesNotContain=" + UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void getAllLocationsByCountriesIsEqualToSomething() throws Exception {
        Countries countries;
        if (TestUtil.findAll(em, Countries.class).isEmpty()) {
            locationsRepository.saveAndFlush(locations);
            countries = CountriesResourceIT.createEntity(em);
        } else {
            countries = TestUtil.findAll(em, Countries.class).get(0);
        }
        em.persist(countries);
        em.flush();
        locations.setCountries(countries);
        locationsRepository.saveAndFlush(locations);
        Long countriesId = countries.getId();

        // Get all the locationsList where countries equals to countriesId
        defaultLocationsShouldBeFound("countriesId.equals=" + countriesId);

        // Get all the locationsList where countries equals to (countriesId + 1)
        defaultLocationsShouldNotBeFound("countriesId.equals=" + (countriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationsShouldBeFound(String filter) throws Exception {
        restLocationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locations.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)));

        // Check, that the count call also returns 1
        restLocationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationsShouldNotBeFound(String filter) throws Exception {
        restLocationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocations() throws Exception {
        // Get the locations
        restLocationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();

        // Update the locations
        Locations updatedLocations = locationsRepository.findById(locations.getId()).get();
        // Disconnect from session so that the updates on updatedLocations are not directly saved in db
        em.detach(updatedLocations);
        updatedLocations
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE);
        LocationsDTO locationsDTO = locationsMapper.toDto(updatedLocations);

        restLocationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
        Locations testLocations = locationsList.get(locationsList.size() - 1);
        assertThat(testLocations.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testLocations.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocations.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocations.getProvince()).isEqualTo(UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void putNonExistingLocations() throws Exception {
        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();
        locations.setId(count.incrementAndGet());

        // Create the Locations
        LocationsDTO locationsDTO = locationsMapper.toDto(locations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocations() throws Exception {
        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();
        locations.setId(count.incrementAndGet());

        // Create the Locations
        LocationsDTO locationsDTO = locationsMapper.toDto(locations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocations() throws Exception {
        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();
        locations.setId(count.incrementAndGet());

        // Create the Locations
        LocationsDTO locationsDTO = locationsMapper.toDto(locations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationsWithPatch() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();

        // Update the locations using partial update
        Locations partialUpdatedLocations = new Locations();
        partialUpdatedLocations.setId(locations.getId());

        partialUpdatedLocations.postalCode(UPDATED_POSTAL_CODE).city(UPDATED_CITY).province(UPDATED_PROVINCE);

        restLocationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocations.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocations))
            )
            .andExpect(status().isOk());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
        Locations testLocations = locationsList.get(locationsList.size() - 1);
        assertThat(testLocations.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testLocations.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocations.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocations.getProvince()).isEqualTo(UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void fullUpdateLocationsWithPatch() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();

        // Update the locations using partial update
        Locations partialUpdatedLocations = new Locations();
        partialUpdatedLocations.setId(locations.getId());

        partialUpdatedLocations
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .province(UPDATED_PROVINCE);

        restLocationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocations.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocations))
            )
            .andExpect(status().isOk());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
        Locations testLocations = locationsList.get(locationsList.size() - 1);
        assertThat(testLocations.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testLocations.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testLocations.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testLocations.getProvince()).isEqualTo(UPDATED_PROVINCE);
    }

    @Test
    @Transactional
    void patchNonExistingLocations() throws Exception {
        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();
        locations.setId(count.incrementAndGet());

        // Create the Locations
        LocationsDTO locationsDTO = locationsMapper.toDto(locations);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocations() throws Exception {
        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();
        locations.setId(count.incrementAndGet());

        // Create the Locations
        LocationsDTO locationsDTO = locationsMapper.toDto(locations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocations() throws Exception {
        int databaseSizeBeforeUpdate = locationsRepository.findAll().size();
        locations.setId(count.incrementAndGet());

        // Create the Locations
        LocationsDTO locationsDTO = locationsMapper.toDto(locations);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Locations in the database
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocations() throws Exception {
        // Initialize the database
        locationsRepository.saveAndFlush(locations);

        int databaseSizeBeforeDelete = locationsRepository.findAll().size();

        // Delete the locations
        restLocationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, locations.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Locations> locationsList = locationsRepository.findAll();
        assertThat(locationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
