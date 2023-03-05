package com.myhotel.company.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myhotel.company.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationsDTO.class);
        LocationsDTO locationsDTO1 = new LocationsDTO();
        locationsDTO1.setId(1L);
        LocationsDTO locationsDTO2 = new LocationsDTO();
        assertThat(locationsDTO1).isNotEqualTo(locationsDTO2);
        locationsDTO2.setId(locationsDTO1.getId());
        assertThat(locationsDTO1).isEqualTo(locationsDTO2);
        locationsDTO2.setId(2L);
        assertThat(locationsDTO1).isNotEqualTo(locationsDTO2);
        locationsDTO1.setId(null);
        assertThat(locationsDTO1).isNotEqualTo(locationsDTO2);
    }
}
