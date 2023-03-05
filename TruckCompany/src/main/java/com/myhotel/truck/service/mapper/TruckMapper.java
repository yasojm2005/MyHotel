package com.myhotel.truck.service.mapper;

import com.myhotel.truck.domain.Truck;
import com.myhotel.truck.service.dto.TruckDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Truck} and its DTO {@link TruckDTO}.
 */
@Mapper(componentModel = "spring")
public interface TruckMapper extends EntityMapper<TruckDTO, Truck> {}
