package com.myhotel.company.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myhotel.company.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountriesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountriesDTO.class);
        CountriesDTO countriesDTO1 = new CountriesDTO();
        countriesDTO1.setId(1L);
        CountriesDTO countriesDTO2 = new CountriesDTO();
        assertThat(countriesDTO1).isNotEqualTo(countriesDTO2);
        countriesDTO2.setId(countriesDTO1.getId());
        assertThat(countriesDTO1).isEqualTo(countriesDTO2);
        countriesDTO2.setId(2L);
        assertThat(countriesDTO1).isNotEqualTo(countriesDTO2);
        countriesDTO1.setId(null);
        assertThat(countriesDTO1).isNotEqualTo(countriesDTO2);
    }
}
