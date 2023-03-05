/*******************************************************************************
 * Copyright (c) 2023 Viking Cloud, LLC - All Rights Reserved.
 *
 * Proprietary and confidential information of Viking Cloud, LLC.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 *******************************************************************************/
package com.myhotel.company.domain.proyections;

import java.math.BigDecimal;

/**
 * TODO: Class description here
 *
 * <pre>
 * Copyright (c) 2023 Viking Cloud, LLC. - All rights reserved.
 * </pre>
 *
 * @author ymartinez
 */
public class SalaryAvgByDepartment {

    private String departmentName;

    private BigDecimal salaryAvg;

    public SalaryAvgByDepartment(String departmentName, BigDecimal salaryAvg) {
        this.departmentName = departmentName;
        this.salaryAvg = salaryAvg;
    }

    public SalaryAvgByDepartment() {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public BigDecimal getSalaryAvg() {
        return salaryAvg;
    }

    public void setSalaryAvg(BigDecimal salaryAvg) {
        this.salaryAvg = salaryAvg;
    }
}

