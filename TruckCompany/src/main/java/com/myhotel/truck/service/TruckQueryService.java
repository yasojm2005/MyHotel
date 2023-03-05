package com.myhotel.truck.service;

import com.myhotel.truck.domain.*; // for static metamodels
import com.myhotel.truck.domain.Truck;
import com.myhotel.truck.repository.TruckRepository;
import com.myhotel.truck.service.criteria.TruckCriteria;
import com.myhotel.truck.service.dto.TruckDTO;
import com.myhotel.truck.service.mapper.TruckMapper;
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
 * Service for executing complex queries for {@link Truck} entities in the database.
 * The main input is a {@link TruckCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TruckDTO} or a {@link Page} of {@link TruckDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TruckQueryService extends QueryService<Truck> {

    private final Logger log = LoggerFactory.getLogger(TruckQueryService.class);

    private final TruckRepository truckRepository;

    private final TruckMapper truckMapper;

    public TruckQueryService(TruckRepository truckRepository, TruckMapper truckMapper) {
        this.truckRepository = truckRepository;
        this.truckMapper = truckMapper;
    }

    /**
     * Return a {@link List} of {@link TruckDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TruckDTO> findByCriteria(TruckCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Truck> specification = createSpecification(criteria);
        return truckMapper.toDto(truckRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TruckDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TruckDTO> findByCriteria(TruckCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Truck> specification = createSpecification(criteria);
        return truckRepository.findAll(specification, page).map(truckMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TruckCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Truck> specification = createSpecification(criteria);
        return truckRepository.count(specification);
    }

    /**
     * Function to convert {@link TruckCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Truck> createSpecification(TruckCriteria criteria) {
        Specification<Truck> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Truck_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Truck_.type));
            }
        }
        return specification;
    }
}
