/*******************************************************************************
 * Copyright (c) 2023 Viking Cloud, LLC - All Rights Reserved.
 *
 * Proprietary and confidential information of Viking Cloud, LLC.
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 *******************************************************************************/
package com.myhotel.company.domain.proyections;

import java.math.BigInteger;

/**
 * TODO: Class description here
 *
 * <pre>
 * Copyright (c) 2023 Viking Cloud, LLC. - All rights reserved.
 * </pre>
 *
 * @author ymartinez
 */
public class EmployeeInDeparmentBySegment {

    private String department;
    private BigInteger segmento_a;
    private BigInteger segmento_b;
    private BigInteger segmento_c;

    public EmployeeInDeparmentBySegment(String department, BigInteger segmento_a, BigInteger segmento_b,BigInteger segmento_c) {
        this.department = department;
        this.segmento_a = segmento_a;
        this.segmento_c = segmento_c;
        this.segmento_b = segmento_b;
    }

    public EmployeeInDeparmentBySegment() {
    }

    public String getDeparment() {
        return department;
    }

    public void setDeparment(String deparment) {
        this.department = deparment;
    }

    public BigInteger getSegmento_a() {
        return segmento_a;
    }

    public void setSegmento_a(BigInteger segmento_a) {
        this.segmento_a = segmento_a;
    }



    public BigInteger getSegmento_c() {
        return segmento_c;
    }

    public void setSegmento_c(BigInteger segmento_c) {
        this.segmento_c = segmento_c;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigInteger getSegmento_b() {
        return segmento_b;
    }

    public void setSegmento_b(BigInteger segmento_b) {
        this.segmento_b = segmento_b;
    }
}

