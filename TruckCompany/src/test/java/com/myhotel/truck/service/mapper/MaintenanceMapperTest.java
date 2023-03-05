package com.myhotel.truck.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaintenanceMapperTest {

    private MaintenanceMapper maintenanceMapper;

    @BeforeEach
    public void setUp() {
        maintenanceMapper = new MaintenanceMapperImpl();
    }
}
