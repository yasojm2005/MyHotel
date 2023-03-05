package com.myhotel.company.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountriesMapperTest {

    private CountriesMapper countriesMapper;

    @BeforeEach
    public void setUp() {
        countriesMapper = new CountriesMapperImpl();
    }
}
