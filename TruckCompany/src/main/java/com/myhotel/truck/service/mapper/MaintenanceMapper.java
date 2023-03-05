package com.myhotel.truck.service.mapper;

import com.myhotel.truck.domain.Maintenance;
import com.myhotel.truck.domain.Vehicle;
import com.myhotel.truck.service.dto.MaintenanceDTO;
import com.myhotel.truck.service.dto.VehicleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Maintenance} and its DTO {@link MaintenanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaintenanceMapper extends EntityMapper<MaintenanceDTO, Maintenance> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleId")
    MaintenanceDTO toDto(Maintenance s);

    @Named("vehicleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleDTO toDtoVehicleId(Vehicle vehicle);
}
