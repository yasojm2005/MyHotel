package com.myhotel.company.repository;

import com.myhotel.company.domain.Employees;
import com.myhotel.company.domain.proyections.EmployeeBySegmentDTO;
import com.myhotel.company.domain.proyections.EmployeeInDeparmentBySegment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA repository for the Employees entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeesRepository extends JpaRepository<Employees, Long>, JpaSpecificationExecutor<Employees> {
    @Transactional
    @Modifying
    @Query("update Employees e set e.salary = ?1 where e.salary = ?2")
    int updateSalaryBySalary(BigDecimal salary, BigDecimal salary1);

    long countBySalary(BigDecimal salary);

    @Query(name = "quantityOfEmploymentBySegment", nativeQuery = true)
    List<EmployeeBySegmentDTO> quantityOfEmploymentBySegment();

    @Query(name = "quantityOfEmploymentInDepartmentBySegment", nativeQuery = true)
    List<EmployeeInDeparmentBySegment> quantityOfEmploymentInDeparmentBySegmentByDepartment();

    @Query(value = "SELECT d.department_name as department,\n" +
        "  SUM(CASE WHEN e.SALARY < 3500 THEN 1 ELSE 0 END) AS segmento_a,\n" +
        "  SUM(CASE WHEN e.SALARY >= 3500 AND e.SALARY < 8000 THEN 1 ELSE 0 END) AS segmneto_b,\n" +
        "  SUM(CASE WHEN e.SALARY >= 8000 THEN 1 ELSE 0 END) AS segmento_c\n" +
        "FROM employees e\n" +
        "JOIN departments d ON e.departments_id = d.id\n" +
        "GROUP BY d.department_name;", nativeQuery = true)
    List<Object[]> quantityOfEmploymentInDeparmentBySegmentByDepartmentV2();

    @Query(value = "SELECT d.department_name, e.first_name \n" +
        "FROM employees e\n" +
        "INNER JOIN departments d ON e.departments_id = d.id\n" +
        "WHERE (e.salary, e.departments_id) IN \n" +
        "    (SELECT MAX(salary), departments_id \n" +
        "     FROM employees \n" +
        "     GROUP BY departments_id)\n" +
        "ORDER BY d.department_name ASC;", nativeQuery = true)
    List<Object[]> getEmployeeMaxSalaryByDepartment();



    @Query(value = "SELECT AVG(e.salary) as Average, d.department_name\n" +
        "FROM employees e\n" +
        "INNER JOIN departments d ON e.departments_id = d.id\n" +
        "GROUP BY d.department_name\n" +
        "HAVING COUNT(*) > 1;", nativeQuery = true)
    List<Object[]> salaryAvgByDepartment();


    @Query(value = "SELECT \n" +
        "    l.countries_id AS country,\n" +
        "    COUNT(e.id) AS num_employees,\n" +
        "    AVG(e.salary) AS avg_salary,\n" +
        "    MAX(e.salary) AS max_salary,\n" +
        "    MIN(e.salary) AS min_salary,\n" +
        "    AVG((DATE_PART('year', CURRENT_DATE) - DATE_PART('year', e.hire_date)) / 365) AS avg_years_of_service\n" +
        "FROM \n" +
        "    employees e\n" +
        "    JOIN departments d ON e.departments_id = d.id\n" +
        "    JOIN locations l ON d.locations_id = l.id\n" +
        "GROUP BY \n" +
        "    l.countries_id\n" +
        "HAVING \n" +
        "    COUNT(e.id) > 10", nativeQuery = true)
    List<Object[]> getInfoOfEmployeeByCountry();













}
