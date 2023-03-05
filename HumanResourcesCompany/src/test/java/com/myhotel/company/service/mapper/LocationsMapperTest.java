package com.myhotel.company.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationsMapperTest {

    private LocationsMapper locationsMapper;

    @BeforeEach
    public void setUp() {
        locationsMapper = new LocationsMapperImpl();
    }
}
