package com.myhotel.company.service.mapper;

import com.myhotel.company.domain.Countries;
import com.myhotel.company.domain.Locations;
import com.myhotel.company.service.dto.CountriesDTO;
import com.myhotel.company.service.dto.LocationsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Locations} and its DTO {@link LocationsDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationsMapper extends EntityMapper<LocationsDTO, Locations> {
    @Mapping(target = "countries", source = "countries", qualifiedByName = "countriesId")
    LocationsDTO toDto(Locations s);

    @Named("countriesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountriesDTO toDtoCountriesId(Countries countries);
}
