package com.myhotel.company.service.mapper;

import com.myhotel.company.domain.Departments;
import com.myhotel.company.domain.Employees;
import com.myhotel.company.service.dto.DepartmentsDTO;
import com.myhotel.company.service.dto.EmployeesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employees} and its DTO {@link EmployeesDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeesMapper extends EntityMapper<EmployeesDTO, Employees> {
    @Mapping(target = "departments", source = "departments", qualifiedByName = "departmentsId")
    EmployeesDTO toDto(Employees s);

    @Named("departmentsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentsDTO toDtoDepartmentsId(Departments departments);
}
