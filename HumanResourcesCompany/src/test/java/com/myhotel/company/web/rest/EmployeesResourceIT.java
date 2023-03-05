package com.myhotel.company.web.rest;

import static com.myhotel.company.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myhotel.company.IntegrationTest;
import com.myhotel.company.domain.Departments;
import com.myhotel.company.domain.Employees;
import com.myhotel.company.repository.EmployeesRepository;
import com.myhotel.company.service.criteria.EmployeesCriteria;
import com.myhotel.company.service.dto.EmployeesDTO;
import com.myhotel.company.service.mapper.EmployeesMapper;
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
 * Integration tests for the {@link EmployeesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeesResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_HIRE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HIRE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_HIRE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALARY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_COMMISION_PCT = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMISION_PCT = new BigDecimal(2);
    private static final BigDecimal SMALLER_COMMISION_PCT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private EmployeesMapper employeesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeesMockMvc;

    private Employees employees;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employees createEntity(EntityManager em) {
        Employees employees = new Employees()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .hireDate(DEFAULT_HIRE_DATE)
            .salary(DEFAULT_SALARY)
            .commisionPCT(DEFAULT_COMMISION_PCT);
        return employees;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employees createUpdatedEntity(EntityManager em) {
        Employees employees = new Employees()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .commisionPCT(UPDATED_COMMISION_PCT);
        return employees;
    }

    @BeforeEach
    public void initTest() {
        employees = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployees() throws Exception {
        int databaseSizeBeforeCreate = employeesRepository.findAll().size();
        // Create the Employees
        EmployeesDTO employeesDTO = employeesMapper.toDto(employees);
        restEmployeesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeCreate + 1);
        Employees testEmployees = employeesList.get(employeesList.size() - 1);
        assertThat(testEmployees.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployees.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployees.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployees.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEmployees.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployees.getSalary()).isEqualByComparingTo(DEFAULT_SALARY);
        assertThat(testEmployees.getCommisionPCT()).isEqualByComparingTo(DEFAULT_COMMISION_PCT);
    }

    @Test
    @Transactional
    void createEmployeesWithExistingId() throws Exception {
        // Create the Employees with an existing ID
        employees.setId(1L);
        EmployeesDTO employeesDTO = employeesMapper.toDto(employees);

        int databaseSizeBeforeCreate = employeesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList
        restEmployeesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employees.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))))
            .andExpect(jsonPath("$.[*].commisionPCT").value(hasItem(sameNumber(DEFAULT_COMMISION_PCT))));
    }

    @Test
    @Transactional
    void getEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get the employees
        restEmployeesMockMvc
            .perform(get(ENTITY_API_URL_ID, employees.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employees.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.salary").value(sameNumber(DEFAULT_SALARY)))
            .andExpect(jsonPath("$.commisionPCT").value(sameNumber(DEFAULT_COMMISION_PCT)));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        Long id = employees.getId();

        defaultEmployeesShouldBeFound("id.equals=" + id);
        defaultEmployeesShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeesShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where firstName equals to DEFAULT_FIRST_NAME
        defaultEmployeesShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the employeesList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeesShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEmployeesShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the employeesList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeesShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where firstName is not null
        defaultEmployeesShouldBeFound("firstName.specified=true");

        // Get all the employeesList where firstName is null
        defaultEmployeesShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where firstName contains DEFAULT_FIRST_NAME
        defaultEmployeesShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the employeesList where firstName contains UPDATED_FIRST_NAME
        defaultEmployeesShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where firstName does not contain DEFAULT_FIRST_NAME
        defaultEmployeesShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the employeesList where firstName does not contain UPDATED_FIRST_NAME
        defaultEmployeesShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where lastName equals to DEFAULT_LAST_NAME
        defaultEmployeesShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the employeesList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeesShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEmployeesShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the employeesList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeesShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where lastName is not null
        defaultEmployeesShouldBeFound("lastName.specified=true");

        // Get all the employeesList where lastName is null
        defaultEmployeesShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where lastName contains DEFAULT_LAST_NAME
        defaultEmployeesShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the employeesList where lastName contains UPDATED_LAST_NAME
        defaultEmployeesShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where lastName does not contain DEFAULT_LAST_NAME
        defaultEmployeesShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the employeesList where lastName does not contain UPDATED_LAST_NAME
        defaultEmployeesShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where email equals to DEFAULT_EMAIL
        defaultEmployeesShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the employeesList where email equals to UPDATED_EMAIL
        defaultEmployeesShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmployeesShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the employeesList where email equals to UPDATED_EMAIL
        defaultEmployeesShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where email is not null
        defaultEmployeesShouldBeFound("email.specified=true");

        // Get all the employeesList where email is null
        defaultEmployeesShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailContainsSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where email contains DEFAULT_EMAIL
        defaultEmployeesShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the employeesList where email contains UPDATED_EMAIL
        defaultEmployeesShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where email does not contain DEFAULT_EMAIL
        defaultEmployeesShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the employeesList where email does not contain UPDATED_EMAIL
        defaultEmployeesShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultEmployeesShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the employeesList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultEmployeesShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultEmployeesShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the employeesList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultEmployeesShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where phoneNumber is not null
        defaultEmployeesShouldBeFound("phoneNumber.specified=true");

        // Get all the employeesList where phoneNumber is null
        defaultEmployeesShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultEmployeesShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the employeesList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultEmployeesShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultEmployeesShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the employeesList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultEmployeesShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where hireDate equals to DEFAULT_HIRE_DATE
        defaultEmployeesShouldBeFound("hireDate.equals=" + DEFAULT_HIRE_DATE);

        // Get all the employeesList where hireDate equals to UPDATED_HIRE_DATE
        defaultEmployeesShouldNotBeFound("hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where hireDate in DEFAULT_HIRE_DATE or UPDATED_HIRE_DATE
        defaultEmployeesShouldBeFound("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE);

        // Get all the employeesList where hireDate equals to UPDATED_HIRE_DATE
        defaultEmployeesShouldNotBeFound("hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where hireDate is not null
        defaultEmployeesShouldBeFound("hireDate.specified=true");

        // Get all the employeesList where hireDate is null
        defaultEmployeesShouldNotBeFound("hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where hireDate is greater than or equal to DEFAULT_HIRE_DATE
        defaultEmployeesShouldBeFound("hireDate.greaterThanOrEqual=" + DEFAULT_HIRE_DATE);

        // Get all the employeesList where hireDate is greater than or equal to UPDATED_HIRE_DATE
        defaultEmployeesShouldNotBeFound("hireDate.greaterThanOrEqual=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where hireDate is less than or equal to DEFAULT_HIRE_DATE
        defaultEmployeesShouldBeFound("hireDate.lessThanOrEqual=" + DEFAULT_HIRE_DATE);

        // Get all the employeesList where hireDate is less than or equal to SMALLER_HIRE_DATE
        defaultEmployeesShouldNotBeFound("hireDate.lessThanOrEqual=" + SMALLER_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where hireDate is less than DEFAULT_HIRE_DATE
        defaultEmployeesShouldNotBeFound("hireDate.lessThan=" + DEFAULT_HIRE_DATE);

        // Get all the employeesList where hireDate is less than UPDATED_HIRE_DATE
        defaultEmployeesShouldBeFound("hireDate.lessThan=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where hireDate is greater than DEFAULT_HIRE_DATE
        defaultEmployeesShouldNotBeFound("hireDate.greaterThan=" + DEFAULT_HIRE_DATE);

        // Get all the employeesList where hireDate is greater than SMALLER_HIRE_DATE
        defaultEmployeesShouldBeFound("hireDate.greaterThan=" + SMALLER_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where salary equals to DEFAULT_SALARY
        defaultEmployeesShouldBeFound("salary.equals=" + DEFAULT_SALARY);

        // Get all the employeesList where salary equals to UPDATED_SALARY
        defaultEmployeesShouldNotBeFound("salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where salary in DEFAULT_SALARY or UPDATED_SALARY
        defaultEmployeesShouldBeFound("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY);

        // Get all the employeesList where salary equals to UPDATED_SALARY
        defaultEmployeesShouldNotBeFound("salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where salary is not null
        defaultEmployeesShouldBeFound("salary.specified=true");

        // Get all the employeesList where salary is null
        defaultEmployeesShouldNotBeFound("salary.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where salary is greater than or equal to DEFAULT_SALARY
        defaultEmployeesShouldBeFound("salary.greaterThanOrEqual=" + DEFAULT_SALARY);

        // Get all the employeesList where salary is greater than or equal to UPDATED_SALARY
        defaultEmployeesShouldNotBeFound("salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where salary is less than or equal to DEFAULT_SALARY
        defaultEmployeesShouldBeFound("salary.lessThanOrEqual=" + DEFAULT_SALARY);

        // Get all the employeesList where salary is less than or equal to SMALLER_SALARY
        defaultEmployeesShouldNotBeFound("salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where salary is less than DEFAULT_SALARY
        defaultEmployeesShouldNotBeFound("salary.lessThan=" + DEFAULT_SALARY);

        // Get all the employeesList where salary is less than UPDATED_SALARY
        defaultEmployeesShouldBeFound("salary.lessThan=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where salary is greater than DEFAULT_SALARY
        defaultEmployeesShouldNotBeFound("salary.greaterThan=" + DEFAULT_SALARY);

        // Get all the employeesList where salary is greater than SMALLER_SALARY
        defaultEmployeesShouldBeFound("salary.greaterThan=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommisionPCTIsEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where commisionPCT equals to DEFAULT_COMMISION_PCT
        defaultEmployeesShouldBeFound("commisionPCT.equals=" + DEFAULT_COMMISION_PCT);

        // Get all the employeesList where commisionPCT equals to UPDATED_COMMISION_PCT
        defaultEmployeesShouldNotBeFound("commisionPCT.equals=" + UPDATED_COMMISION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommisionPCTIsInShouldWork() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where commisionPCT in DEFAULT_COMMISION_PCT or UPDATED_COMMISION_PCT
        defaultEmployeesShouldBeFound("commisionPCT.in=" + DEFAULT_COMMISION_PCT + "," + UPDATED_COMMISION_PCT);

        // Get all the employeesList where commisionPCT equals to UPDATED_COMMISION_PCT
        defaultEmployeesShouldNotBeFound("commisionPCT.in=" + UPDATED_COMMISION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommisionPCTIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where commisionPCT is not null
        defaultEmployeesShouldBeFound("commisionPCT.specified=true");

        // Get all the employeesList where commisionPCT is null
        defaultEmployeesShouldNotBeFound("commisionPCT.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByCommisionPCTIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where commisionPCT is greater than or equal to DEFAULT_COMMISION_PCT
        defaultEmployeesShouldBeFound("commisionPCT.greaterThanOrEqual=" + DEFAULT_COMMISION_PCT);

        // Get all the employeesList where commisionPCT is greater than or equal to UPDATED_COMMISION_PCT
        defaultEmployeesShouldNotBeFound("commisionPCT.greaterThanOrEqual=" + UPDATED_COMMISION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommisionPCTIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where commisionPCT is less than or equal to DEFAULT_COMMISION_PCT
        defaultEmployeesShouldBeFound("commisionPCT.lessThanOrEqual=" + DEFAULT_COMMISION_PCT);

        // Get all the employeesList where commisionPCT is less than or equal to SMALLER_COMMISION_PCT
        defaultEmployeesShouldNotBeFound("commisionPCT.lessThanOrEqual=" + SMALLER_COMMISION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommisionPCTIsLessThanSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where commisionPCT is less than DEFAULT_COMMISION_PCT
        defaultEmployeesShouldNotBeFound("commisionPCT.lessThan=" + DEFAULT_COMMISION_PCT);

        // Get all the employeesList where commisionPCT is less than UPDATED_COMMISION_PCT
        defaultEmployeesShouldBeFound("commisionPCT.lessThan=" + UPDATED_COMMISION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByCommisionPCTIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        // Get all the employeesList where commisionPCT is greater than DEFAULT_COMMISION_PCT
        defaultEmployeesShouldNotBeFound("commisionPCT.greaterThan=" + DEFAULT_COMMISION_PCT);

        // Get all the employeesList where commisionPCT is greater than SMALLER_COMMISION_PCT
        defaultEmployeesShouldBeFound("commisionPCT.greaterThan=" + SMALLER_COMMISION_PCT);
    }

    @Test
    @Transactional
    void getAllEmployeesByDepartmentsIsEqualToSomething() throws Exception {
        Departments departments;
        if (TestUtil.findAll(em, Departments.class).isEmpty()) {
            employeesRepository.saveAndFlush(employees);
            departments = DepartmentsResourceIT.createEntity(em);
        } else {
            departments = TestUtil.findAll(em, Departments.class).get(0);
        }
        em.persist(departments);
        em.flush();
        employees.setDepartments(departments);
        employeesRepository.saveAndFlush(employees);
        Long departmentsId = departments.getId();

        // Get all the employeesList where departments equals to departmentsId
        defaultEmployeesShouldBeFound("departmentsId.equals=" + departmentsId);

        // Get all the employeesList where departments equals to (departmentsId + 1)
        defaultEmployeesShouldNotBeFound("departmentsId.equals=" + (departmentsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeesShouldBeFound(String filter) throws Exception {
        restEmployeesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employees.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))))
            .andExpect(jsonPath("$.[*].commisionPCT").value(hasItem(sameNumber(DEFAULT_COMMISION_PCT))));

        // Check, that the count call also returns 1
        restEmployeesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeesShouldNotBeFound(String filter) throws Exception {
        restEmployeesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployees() throws Exception {
        // Get the employees
        restEmployeesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();

        // Update the employees
        Employees updatedEmployees = employeesRepository.findById(employees.getId()).get();
        // Disconnect from session so that the updates on updatedEmployees are not directly saved in db
        em.detach(updatedEmployees);
        updatedEmployees
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .commisionPCT(UPDATED_COMMISION_PCT);
        EmployeesDTO employeesDTO = employeesMapper.toDto(updatedEmployees);

        restEmployeesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
        Employees testEmployees = employeesList.get(employeesList.size() - 1);
        assertThat(testEmployees.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployees.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployees.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployees.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployees.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployees.getSalary()).isEqualByComparingTo(UPDATED_SALARY);
        assertThat(testEmployees.getCommisionPCT()).isEqualByComparingTo(UPDATED_COMMISION_PCT);
    }

    @Test
    @Transactional
    void putNonExistingEmployees() throws Exception {
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();
        employees.setId(count.incrementAndGet());

        // Create the Employees
        EmployeesDTO employeesDTO = employeesMapper.toDto(employees);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployees() throws Exception {
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();
        employees.setId(count.incrementAndGet());

        // Create the Employees
        EmployeesDTO employeesDTO = employeesMapper.toDto(employees);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployees() throws Exception {
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();
        employees.setId(count.incrementAndGet());

        // Create the Employees
        EmployeesDTO employeesDTO = employeesMapper.toDto(employees);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeesWithPatch() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();

        // Update the employees using partial update
        Employees partialUpdatedEmployees = new Employees();
        partialUpdatedEmployees.setId(employees.getId());

        partialUpdatedEmployees
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .salary(UPDATED_SALARY);

        restEmployeesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployees.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployees))
            )
            .andExpect(status().isOk());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
        Employees testEmployees = employeesList.get(employeesList.size() - 1);
        assertThat(testEmployees.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployees.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployees.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployees.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployees.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployees.getSalary()).isEqualByComparingTo(UPDATED_SALARY);
        assertThat(testEmployees.getCommisionPCT()).isEqualByComparingTo(DEFAULT_COMMISION_PCT);
    }

    @Test
    @Transactional
    void fullUpdateEmployeesWithPatch() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();

        // Update the employees using partial update
        Employees partialUpdatedEmployees = new Employees();
        partialUpdatedEmployees.setId(employees.getId());

        partialUpdatedEmployees
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .commisionPCT(UPDATED_COMMISION_PCT);

        restEmployeesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployees.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployees))
            )
            .andExpect(status().isOk());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
        Employees testEmployees = employeesList.get(employeesList.size() - 1);
        assertThat(testEmployees.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployees.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployees.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployees.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEmployees.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployees.getSalary()).isEqualByComparingTo(UPDATED_SALARY);
        assertThat(testEmployees.getCommisionPCT()).isEqualByComparingTo(UPDATED_COMMISION_PCT);
    }

    @Test
    @Transactional
    void patchNonExistingEmployees() throws Exception {
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();
        employees.setId(count.incrementAndGet());

        // Create the Employees
        EmployeesDTO employeesDTO = employeesMapper.toDto(employees);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployees() throws Exception {
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();
        employees.setId(count.incrementAndGet());

        // Create the Employees
        EmployeesDTO employeesDTO = employeesMapper.toDto(employees);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployees() throws Exception {
        int databaseSizeBeforeUpdate = employeesRepository.findAll().size();
        employees.setId(count.incrementAndGet());

        // Create the Employees
        EmployeesDTO employeesDTO = employeesMapper.toDto(employees);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employees in the database
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployees() throws Exception {
        // Initialize the database
        employeesRepository.saveAndFlush(employees);

        int databaseSizeBeforeDelete = employeesRepository.findAll().size();

        // Delete the employees
        restEmployeesMockMvc
            .perform(delete(ENTITY_API_URL_ID, employees.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employees> employeesList = employeesRepository.findAll();
        assertThat(employeesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
