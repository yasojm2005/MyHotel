package com.myhotel.company.service.impl;

import com.myhotel.company.domain.Locations;
import com.myhotel.company.repository.LocationsRepository;
import com.myhotel.company.service.LocationsService;
import com.myhotel.company.service.dto.LocationsDTO;
import com.myhotel.company.service.mapper.LocationsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Locations}.
 */
@Service
@Transactional
public class LocationsServiceImpl implements LocationsService {

    private final Logger log = LoggerFactory.getLogger(LocationsServiceImpl.class);

    private final LocationsRepository locationsRepository;

    private final LocationsMapper locationsMapper;

    public LocationsServiceImpl(LocationsRepository locationsRepository, LocationsMapper locationsMapper) {
        this.locationsRepository = locationsRepository;
        this.locationsMapper = locationsMapper;
    }

    @Override
    public LocationsDTO save(LocationsDTO locationsDTO) {
        log.debug("Request to save Locations : {}", locationsDTO);
        Locations locations = locationsMapper.toEntity(locationsDTO);
        locations = locationsRepository.save(locations);
        return locationsMapper.toDto(locations);
    }

    @Override
    public LocationsDTO update(LocationsDTO locationsDTO) {
        log.debug("Request to update Locations : {}", locationsDTO);
        Locations locations = locationsMapper.toEntity(locationsDTO);
        locations = locationsRepository.save(locations);
        return locationsMapper.toDto(locations);
    }

    @Override
    public Optional<LocationsDTO> partialUpdate(LocationsDTO locationsDTO) {
        log.debug("Request to partially update Locations : {}", locationsDTO);

        return locationsRepository
            .findById(locationsDTO.getId())
            .map(existingLocations -> {
                locationsMapper.partialUpdate(existingLocations, locationsDTO);

                return existingLocations;
            })
            .map(locationsRepository::save)
            .map(locationsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocationsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationsRepository.findAll(pageable).map(locationsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationsDTO> findOne(Long id) {
        log.debug("Request to get Locations : {}", id);
        return locationsRepository.findById(id).map(locationsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Locations : {}", id);
        locationsRepository.deleteById(id);
    }
}
