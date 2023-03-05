package com.myhotel.truck.service;

import com.myhotel.truck.domain.*; // for static metamodels
import com.myhotel.truck.domain.Maintenance;
import com.myhotel.truck.repository.MaintenanceRepository;
import com.myhotel.truck.service.criteria.MaintenanceCriteria;
import com.myhotel.truck.service.dto.MaintenanceDTO;
import com.myhotel.truck.service.mapper.MaintenanceMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Maintenance} entities in the database.
 * The main input is a {@link MaintenanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MaintenanceDTO} or a {@link Page} of {@link MaintenanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaintenanceQueryService extends QueryService<Maintenance> {

    private final Logger log = LoggerFactory.getLogger(MaintenanceQueryService.class);

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceQueryService(MaintenanceRepository maintenanceRepository, MaintenanceMapper maintenanceMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceMapper = maintenanceMapper;
    }

    /**
     * Return a {@link List} of {@link MaintenanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findByCriteria(MaintenanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Maintenance> specification = createSpecification(criteria);
        return maintenanceMapper.toDto(maintenanceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MaintenanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaintenanceDTO> findByCriteria(MaintenanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Maintenance> specification = createSpecification(criteria);
        return maintenanceRepository.findAll(specification, page).map(maintenanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaintenanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Maintenance> specification = createSpecification(criteria);
        return maintenanceRepository.count(specification);
    }

    /**
     * Function to convert {@link MaintenanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Maintenance> createSpecification(MaintenanceCriteria criteria) {
        Specification<Maintenance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Maintenance_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Maintenance_.date));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Maintenance_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Maintenance_.description));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Maintenance_.price));
            }
            if (criteria.getVehicleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVehicleId(), root -> root.join(Maintenance_.vehicle, JoinType.LEFT).get(Vehicle_.id))
                    );
            }
        }
        return specification;
    }
}
