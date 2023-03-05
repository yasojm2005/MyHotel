package com.myhotel.company.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeesMapperTest {

    private EmployeesMapper employeesMapper;

    @BeforeEach
    public void setUp() {
        employeesMapper = new EmployeesMapperImpl();
    }
}
