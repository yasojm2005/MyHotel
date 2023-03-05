package com.myhotel.company.service;

import com.myhotel.company.domain.proyections.EmployeeBySegmentDTO;
import com.myhotel.company.domain.proyections.EmployeeInDeparmentBySegment;
import com.myhotel.company.domain.proyections.EmployeeInfoByCountry;
import com.myhotel.company.domain.proyections.EmployeeMaxSalaryByDepartment;
import com.myhotel.company.domain.proyections.SalaryAvgByDepartment;
import com.myhotel.company.service.dto.EmployeesDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.myhotel.company.domain.Employees}.
 */
public interface EmployeesService {
    /**
     * Save a employees.
     *
     * @param employeesDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeesDTO save(EmployeesDTO employeesDTO);

    /**
     * Updates a employees.
     *
     * @param employeesDTO the entity to update.
     * @return the persisted entity.
     */
    EmployeesDTO update(EmployeesDTO employeesDTO);

    /**
     * Partially updates a employees.
     *
     * @param employeesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmployeesDTO> partialUpdate(EmployeesDTO employeesDTO);

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmployeesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" employees.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeesDTO> findOne(Long id);

    /**
     * Delete the "id" employees.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<EmployeeBySegmentDTO> quantityOfEmploymentBySegment();

    List<EmployeeInDeparmentBySegment> quantityOfEmploymentInDepartmentBySegmentByDepartmentV2();


    List<EmployeeMaxSalaryByDepartment> getEmployeeMaxSalaryByDepartment();

    List<SalaryAvgByDepartment> salaryAvgByDepartment();


    List<EmployeeInfoByCountry> getInfoOfEmployeeByCountry();
















}
