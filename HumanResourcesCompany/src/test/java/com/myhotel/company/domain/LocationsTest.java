package com.myhotel.company.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myhotel.company.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Locations.class);
        Locations locations1 = new Locations();
        locations1.setId(1L);
        Locations locations2 = new Locations();
        locations2.setId(locations1.getId());
        assertThat(locations1).isEqualTo(locations2);
        locations2.setId(2L);
        assertThat(locations1).isNotEqualTo(locations2);
        locations1.setId(null);
        assertThat(locations1).isNotEqualTo(locations2);
    }
}
