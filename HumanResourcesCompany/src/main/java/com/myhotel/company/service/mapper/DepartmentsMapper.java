package com.myhotel.company.service.mapper;

import com.myhotel.company.domain.Departments;
import com.myhotel.company.domain.Locations;
import com.myhotel.company.service.dto.DepartmentsDTO;
import com.myhotel.company.service.dto.LocationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departments} and its DTO {@link DepartmentsDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartmentsMapper extends EntityMapper<DepartmentsDTO, Departments> {
    @Mapping(target = "locations", source = "locations", qualifiedByName = "locationsId")
    DepartmentsDTO toDto(Departments s);

    @Named("locationsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationsDTO toDtoLocationsId(Locations locations);
}
