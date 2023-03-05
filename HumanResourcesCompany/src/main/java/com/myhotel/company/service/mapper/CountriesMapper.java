package com.myhotel.company.service.mapper;

import com.myhotel.company.domain.Countries;
import com.myhotel.company.domain.Region;
import com.myhotel.company.service.dto.CountriesDTO;
import com.myhotel.company.service.dto.RegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Countries} and its DTO {@link CountriesDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountriesMapper extends EntityMapper<CountriesDTO, Countries> {
    @Mapping(target = "region", source = "region", qualifiedByName = "regionId")
    CountriesDTO toDto(Countries s);

    @Named("regionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegionDTO toDtoRegionId(Region region);
}
