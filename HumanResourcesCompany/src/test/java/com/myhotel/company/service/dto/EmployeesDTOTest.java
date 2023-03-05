package com.myhotel.company.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myhotel.company.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmployeesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeesDTO.class);
        EmployeesDTO employeesDTO1 = new EmployeesDTO();
        employeesDTO1.setId(1L);
        EmployeesDTO employeesDTO2 = new EmployeesDTO();
        assertThat(employeesDTO1).isNotEqualTo(employeesDTO2);
        employeesDTO2.setId(employeesDTO1.getId());
        assertThat(employeesDTO1).isEqualTo(employeesDTO2);
        employeesDTO2.setId(2L);
        assertThat(employeesDTO1).isNotEqualTo(employeesDTO2);
        employeesDTO1.setId(null);
        assertThat(employeesDTO1).isNotEqualTo(employeesDTO2);
    }
}
