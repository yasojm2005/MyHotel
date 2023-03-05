/*******************************************************************************
 * Copyright (c) 2023 Viking Cloud, LLC - All Rights Reserved.
 *
 * Proprietary and confidential information of Viking Cloud, LLC.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 *******************************************************************************/
package com.myhotel.company.web.rest;

import com.myhotel.company.domain.proyections.EmployeeBySegmentDTO;
import com.myhotel.company.domain.proyections.EmployeeInDeparmentBySegment;
import com.myhotel.company.domain.proyections.EmployeeInfoByCountry;
import com.myhotel.company.domain.proyections.EmployeeMaxSalaryByDepartment;
import com.myhotel.company.domain.proyections.SalaryAvgByDepartment;
import com.myhotel.company.repository.EmployeesRepository;
import com.myhotel.company.service.EmployeesQueryService;
import com.myhotel.company.service.EmployeesService;
import com.myhotel.company.service.dto.EmployeesDTO;
import com.myhotel.company.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * TODO: Class description here
 *
 * <pre>
 * Copyright (c) 2023 Viking Cloud, LLC. - All rights reserved.
 * </pre>
 *
 * @author ymartinez
 */
@RestController
@RequestMapping("/api/report")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(EmployeesResource.class);


    private final EmployeesService employeesService;

    public ReportResource(
        EmployeesService employeesService
    ) {
        this.employeesService = employeesService;
    }

    @GetMapping("/employees_by_segment")
    public ResponseEntity<List<EmployeeBySegmentDTO>> quantityOfEmploymentBySegment(
         ) throws URISyntaxException {
        log.debug("REST request to get employees by segment");

        List<EmployeeBySegmentDTO> quantityOfEmployment =   employeesService.quantityOfEmploymentBySegment();
        return ResponseEntity
            .ok()
            .body(quantityOfEmployment);
    }

    @GetMapping("/employees_in_department_by_segment")
    public ResponseEntity<List<EmployeeInDeparmentBySegment>> quantityOfEmploymentInDepartmentBySegment(
    ) {
        log.debug("REST request to get employees in department  by segment");

        List<EmployeeInDeparmentBySegment> employeeInDeparmentBySegments =   employeesService.quantityOfEmploymentInDepartmentBySegmentByDepartmentV2();
        return ResponseEntity
            .ok()
            .body(employeeInDeparmentBySegments);
    }

    @GetMapping("/employees_max_salary_by_department")
    public ResponseEntity<List<EmployeeMaxSalaryByDepartment>> getEmployeeMaxSalaryByDepartment(
    ) {
        log.debug("REST request to get employees by segment");

        List<EmployeeMaxSalaryByDepartment> employeeMaxSalaryByDepartment =   employeesService.getEmployeeMaxSalaryByDepartment();
        return ResponseEntity
            .ok()
            .body(employeeMaxSalaryByDepartment);
    }

    @GetMapping("/departments_salary")
    public ResponseEntity<List<SalaryAvgByDepartment>> getSalaryAverageByDepartment(
    ) {
        log.debug("REST request to get employees by segment");

        List<SalaryAvgByDepartment> salaryAvgByDepartmentList =   employeesService.salaryAvgByDepartment();
        return ResponseEntity
            .ok()
            .body(salaryAvgByDepartmentList);
    }



    @GetMapping("/employee_info")
    public ResponseEntity<List<EmployeeInfoByCountry>> getEmployeeInfoByCountry(
    ) {
        log.debug("REST request to get employees by segment");

        List<EmployeeInfoByCountry> employeeInfoByCountries =   employeesService.getInfoOfEmployeeByCountry();
        return ResponseEntity
            .ok()
            .body(employeeInfoByCountries);
    }


}

