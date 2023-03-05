package com.myhotel.company.service.impl;

import com.myhotel.company.domain.Departments;
import com.myhotel.company.repository.DepartmentsRepository;
import com.myhotel.company.service.DepartmentsService;
import com.myhotel.company.service.dto.DepartmentsDTO;
import com.myhotel.company.service.mapper.DepartmentsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departments}.
 */
@Service
@Transactional
public class DepartmentsServiceImpl implements DepartmentsService {

    private final Logger log = LoggerFactory.getLogger(DepartmentsServiceImpl.class);

    private final DepartmentsRepository departmentsRepository;

    private final DepartmentsMapper departmentsMapper;

    public DepartmentsServiceImpl(DepartmentsRepository departmentsRepository, DepartmentsMapper departmentsMapper) {
        this.departmentsRepository = departmentsRepository;
        this.departmentsMapper = departmentsMapper;
    }

    @Override
    public DepartmentsDTO save(DepartmentsDTO departmentsDTO) {
        log.debug("Request to save Departments : {}", departmentsDTO);
        Departments departments = departmentsMapper.toEntity(departmentsDTO);
        departments = departmentsRepository.save(departments);
        return departmentsMapper.toDto(departments);
    }

    @Override
    public DepartmentsDTO update(DepartmentsDTO departmentsDTO) {
        log.debug("Request to update Departments : {}", departmentsDTO);
        Departments departments = departmentsMapper.toEntity(departmentsDTO);
        departments = departmentsRepository.save(departments);
        return departmentsMapper.toDto(departments);
    }

    @Override
    public Optional<DepartmentsDTO> partialUpdate(DepartmentsDTO departmentsDTO) {
        log.debug("Request to partially update Departments : {}", departmentsDTO);

        return departmentsRepository
            .findById(departmentsDTO.getId())
            .map(existingDepartments -> {
                departmentsMapper.partialUpdate(existingDepartments, departmentsDTO);

                return existingDepartments;
            })
            .map(departmentsRepository::save)
            .map(departmentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Departments");
        return departmentsRepository.findAll(pageable).map(departmentsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentsDTO> findOne(Long id) {
        log.debug("Request to get Departments : {}", id);
        return departmentsRepository.findById(id).map(departmentsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departments : {}", id);
        departmentsRepository.deleteById(id);
    }
}
