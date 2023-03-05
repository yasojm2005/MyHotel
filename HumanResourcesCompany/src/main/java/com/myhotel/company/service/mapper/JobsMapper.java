package com.myhotel.company.service.mapper;

import com.myhotel.company.domain.Employees;
import com.myhotel.company.domain.Jobs;
import com.myhotel.company.service.dto.EmployeesDTO;
import com.myhotel.company.service.dto.JobsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Jobs} and its DTO {@link JobsDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobsMapper extends EntityMapper<JobsDTO, Jobs> {
    @Mapping(target = "jobHistories", source = "jobHistories", qualifiedByName = "employeesIdSet")
    JobsDTO toDto(Jobs s);

    @Mapping(target = "removeJobHistory", ignore = true)
    Jobs toEntity(JobsDTO jobsDTO);

    @Named("employeesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeesDTO toDtoEmployeesId(Employees employees);

    @Named("employeesIdSet")
    default Set<EmployeesDTO> toDtoEmployeesIdSet(Set<Employees> employees) {
        return employees.stream().map(this::toDtoEmployeesId).collect(Collectors.toSet());
    }
}
