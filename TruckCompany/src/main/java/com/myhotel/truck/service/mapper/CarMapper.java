package com.myhotel.truck.service.mapper;

import com.myhotel.truck.domain.Car;
import com.myhotel.truck.service.dto.CarDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Car} and its DTO {@link CarDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarMapper extends EntityMapper<CarDTO, Car> {}
