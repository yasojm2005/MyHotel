package com.myhotel.company.service.impl;

import com.myhotel.company.domain.Employees;
import com.myhotel.company.domain.proyections.EmployeeBySegmentDTO;
import com.myhotel.company.domain.proyections.EmployeeInDeparmentBySegment;
import com.myhotel.company.domain.proyections.EmployeeInfoByCountry;
import com.myhotel.company.domain.proyections.EmployeeMaxSalaryByDepartment;
import com.myhotel.company.domain.proyections.SalaryAvgByDepartment;
import com.myhotel.company.repository.EmployeesRepository;
import com.myhotel.company.service.EmployeesService;
import com.myhotel.company.service.dto.EmployeesDTO;
import com.myhotel.company.service.mapper.EmployeesMapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employees}.
 */
@Service
@Transactional
public class EmployeesServiceImpl implements EmployeesService {

    private final Logger log = LoggerFactory.getLogger(EmployeesServiceImpl.class);

    private final EmployeesRepository employeesRepository;

    private final EmployeesMapper employeesMapper;

    public EmployeesServiceImpl(EmployeesRepository employeesRepository, EmployeesMapper employeesMapper) {
        this.employeesRepository = employeesRepository;
        this.employeesMapper = employeesMapper;
    }

    @Override
    public EmployeesDTO save(EmployeesDTO employeesDTO) {
        log.debug("Request to save Employees : {}", employeesDTO);
        Employees employees = employeesMapper.toEntity(employeesDTO);
        employees = employeesRepository.save(employees);
        return employeesMapper.toDto(employees);
    }

    @Override
    public EmployeesDTO update(EmployeesDTO employeesDTO) {
        log.debug("Request to update Employees : {}", employeesDTO);
        Employees employees = employeesMapper.toEntity(employeesDTO);
        employees = employeesRepository.save(employees);
        return employeesMapper.toDto(employees);
    }

    @Override
    public Optional<EmployeesDTO> partialUpdate(EmployeesDTO employeesDTO) {
        log.debug("Request to partially update Employees : {}", employeesDTO);

        return employeesRepository
            .findById(employeesDTO.getId())
            .map(existingEmployees -> {
                employeesMapper.partialUpdate(existingEmployees, employeesDTO);

                return existingEmployees;
            })
            .map(employeesRepository::save)
            .map(employeesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeesRepository.findAll(pageable).map(employeesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeesDTO> findOne(Long id) {
        log.debug("Request to get Employees : {}", id);
        List<Object[]> listab = employeesRepository.getEmployeeMaxSalaryByDepartment();
        List<Object[]> listac = employeesRepository.salaryAvgByDepartment();
        List<Object[]> listad = employeesRepository.getInfoOfEmployeeByCountry();



        return employeesRepository.findById(id).map(employeesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employees : {}", id);
        employeesRepository.deleteById(id);
    }

    @Override
    public List<EmployeeBySegmentDTO> quantityOfEmploymentBySegment() {
        log.debug("Request get quantity off employment by segment");
        return employeesRepository.quantityOfEmploymentBySegment();
    }

    @Override
    public List<EmployeeInDeparmentBySegment> quantityOfEmploymentInDepartmentBySegmentByDepartmentV2() {
        log.debug("Request get quantity off employment by department ");
        List<EmployeeInDeparmentBySegment> employeeInDeparmentBySegments = new ArrayList<>();
        List<Object[]> employeeByDepartmentAndSegment = employeesRepository.quantityOfEmploymentInDeparmentBySegmentByDepartmentV2();

        employeeByDepartmentAndSegment.forEach(objects -> {
            EmployeeInDeparmentBySegment employeeInDeparmentBySegment = new EmployeeInDeparmentBySegment();
            if(objects[0] != null){
                employeeInDeparmentBySegment.setDeparment((String) objects[0]);
            }
            if(objects[1] != null){
                employeeInDeparmentBySegment.setSegmento_a((BigInteger) objects[1]);
            }
            if(objects[2] != null){
                employeeInDeparmentBySegment.setSegmento_b((BigInteger) objects[2]);
            }
            if(objects[3] != null){
                employeeInDeparmentBySegment.setSegmento_c((BigInteger) objects[3]);
            }
            employeeInDeparmentBySegments.add(employeeInDeparmentBySegment);

        });

        return employeeInDeparmentBySegments;
    }

    @Override
    public List<EmployeeMaxSalaryByDepartment> getEmployeeMaxSalaryByDepartment() {

        log.debug("Request get quantity off employment by department ");
        List<EmployeeMaxSalaryByDepartment> employeeMaxSalaryByDepartment = new ArrayList<>();
        List<Object[]> employeeMaxSalaryByDepartmentSQlResult = employeesRepository.getEmployeeMaxSalaryByDepartment();

        employeeMaxSalaryByDepartmentSQlResult.forEach(objects -> {
            EmployeeMaxSalaryByDepartment employeeInDepartmentBySegment = new EmployeeMaxSalaryByDepartment();
            if(objects[0] != null){
                employeeInDepartmentBySegment.setDepartmentName((String)objects[0]);
            }
            if(objects[1] != null) {
                employeeInDepartmentBySegment.setEmployeeName((String) objects[1]);
            }
            employeeMaxSalaryByDepartment.add(employeeInDepartmentBySegment);

        });
        return employeeMaxSalaryByDepartment;
    }

    @Override
    public List<SalaryAvgByDepartment> salaryAvgByDepartment() {
        log.debug("Request get salary avg by department ");
        List<SalaryAvgByDepartment> salaryAvgByDepartments = new ArrayList<>();
        List<Object[]> salaryAvgByDepartmentSQLResult = employeesRepository.salaryAvgByDepartment();

        salaryAvgByDepartmentSQLResult.forEach(objects -> {
            SalaryAvgByDepartment SalaryAvgByDepartment = new SalaryAvgByDepartment();
            if(objects[0] != null){
                SalaryAvgByDepartment.setSalaryAvg((BigDecimal)objects[0]);
            }
            if(objects[1] != null) {
                SalaryAvgByDepartment.setDepartmentName((String) objects[1]);
            }
            salaryAvgByDepartments.add(SalaryAvgByDepartment);

        });
        return salaryAvgByDepartments;
    }

    @Override
    public List<EmployeeInfoByCountry> getInfoOfEmployeeByCountry() {
        log.debug("Request get salary avg by department ");
        List<EmployeeInfoByCountry> employeeInfoByCountryArrayList = new ArrayList<>();
        List<Object[]> employeeInfoByCountrySQLResult = employeesRepository.getInfoOfEmployeeByCountry();

        employeeInfoByCountrySQLResult.forEach(objects -> {
            EmployeeInfoByCountry employeeInfoByCountry = new EmployeeInfoByCountry();
            if(objects[0] != null){
                employeeInfoByCountry.setCountry((String) objects[0]);
            }
            if(objects[1] != null) {
                employeeInfoByCountry.setEmployeesNumber((Integer) objects[1]);
            }
            if(objects[2] != null) {
                employeeInfoByCountry.setSalaryAvg((Double) objects[2]);
            }
            if(objects[3] != null) {
                employeeInfoByCountry.setMaxSalary((Double) objects[3]);
            }
            if(objects[4] != null) {
                employeeInfoByCountry.setMinSalary((Double) objects[4]);
            }
            employeeInfoByCountryArrayList.add(employeeInfoByCountry);

        });
        return employeeInfoByCountryArrayList;
    }
}
