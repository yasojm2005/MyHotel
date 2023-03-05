/*******************************************************************************
 * Copyright (c) 2023 Viking Cloud, LLC - All Rights Reserved.
 *
 * Proprietary and confidential information of Viking Cloud, LLC.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 *******************************************************************************/
package com.myhotel.company.domain.proyections;

/**
 * TODO: Class description here
 *
 * <pre>
 * Copyright (c) 2023 Viking Cloud, LLC. - All rights reserved.
 * </pre>
 *
 * @author ymartinez
 */
public class EmployeeInfoByCountry {
    private String country;
    private Integer employeesNumber;
    private Double salaryAvg;
    private Double maxSalary;
    private  Double minSalary;
    private Double avgYearsOfServices;

    public EmployeeInfoByCountry(String country, Integer employeesNumber, Double salaryAvg, Double maxSalary, Double minSalary, Double  avgYearsOfServices) {
        this.country = country;
        this.employeesNumber = employeesNumber;
        this.salaryAvg = salaryAvg;
        this.maxSalary = maxSalary;
        this.minSalary = minSalary;
        this.avgYearsOfServices = avgYearsOfServices;
    }

    public EmployeeInfoByCountry() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getEmployeesNumber() {
        return employeesNumber;
    }

    public void setEmployeesNumber(Integer employeesNumber) {
        this.employeesNumber = employeesNumber;
    }

    public Double getSalaryAvg() {
        return salaryAvg;
    }

    public void setSalaryAvg(Double salaryAvg) {
        this.salaryAvg = salaryAvg;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getAvgYearsOfServices() {
        return avgYearsOfServices;
    }

    public void setAvgYearsOfServices(Double avgYearsOfServices) {
        this.avgYearsOfServices = avgYearsOfServices;
    }
}

