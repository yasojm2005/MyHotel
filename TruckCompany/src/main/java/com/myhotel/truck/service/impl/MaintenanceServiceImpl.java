package com.myhotel.truck.service.impl;

import com.myhotel.truck.domain.Maintenance;
import com.myhotel.truck.repository.MaintenanceRepository;
import com.myhotel.truck.service.MaintenanceService;
import com.myhotel.truck.service.dto.MaintenanceDTO;
import com.myhotel.truck.service.mapper.MaintenanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Maintenance}.
 */
@Service
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final Logger log = LoggerFactory.getLogger(MaintenanceServiceImpl.class);

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository, MaintenanceMapper maintenanceMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceMapper = maintenanceMapper;
    }

    @Override
    public MaintenanceDTO save(MaintenanceDTO maintenanceDTO) {
        log.debug("Request to save Maintenance : {}", maintenanceDTO);
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDTO);
        maintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(maintenance);
    }

    @Override
    public MaintenanceDTO update(MaintenanceDTO maintenanceDTO) {
        log.debug("Request to update Maintenance : {}", maintenanceDTO);
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDTO);
        maintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(maintenance);
    }

    @Override
    public Optional<MaintenanceDTO> partialUpdate(MaintenanceDTO maintenanceDTO) {
        log.debug("Request to partially update Maintenance : {}", maintenanceDTO);

        return maintenanceRepository
            .findById(maintenanceDTO.getId())
            .map(existingMaintenance -> {
                maintenanceMapper.partialUpdate(existingMaintenance, maintenanceDTO);

                return existingMaintenance;
            })
            .map(maintenanceRepository::save)
            .map(maintenanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintenanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Maintenances");
        return maintenanceRepository.findAll(pageable).map(maintenanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaintenanceDTO> findOne(Long id) {
        log.debug("Request to get Maintenance : {}", id);
        return maintenanceRepository.findById(id).map(maintenanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Maintenance : {}", id);
        maintenanceRepository.deleteById(id);
    }
}
