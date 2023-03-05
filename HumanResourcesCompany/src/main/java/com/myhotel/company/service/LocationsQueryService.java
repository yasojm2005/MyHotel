package com.myhotel.company.service;

import com.myhotel.company.domain.*; // for static metamodels
import com.myhotel.company.domain.Locations;
import com.myhotel.company.repository.LocationsRepository;
import com.myhotel.company.service.criteria.LocationsCriteria;
import com.myhotel.company.service.dto.LocationsDTO;
import com.myhotel.company.service.mapper.LocationsMapper;
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
 * Service for executing complex queries for {@link Locations} entities in the database.
 * The main input is a {@link LocationsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LocationsDTO} or a {@link Page} of {@link LocationsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocationsQueryService extends QueryService<Locations> {

    private final Logger log = LoggerFactory.getLogger(LocationsQueryService.class);

    private final LocationsRepository locationsRepository;

    private final LocationsMapper locationsMapper;

    public LocationsQueryService(LocationsRepository locationsRepository, LocationsMapper locationsMapper) {
        this.locationsRepository = locationsRepository;
        this.locationsMapper = locationsMapper;
    }

    /**
     * Return a {@link List} of {@link LocationsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LocationsDTO> findByCriteria(LocationsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Locations> specification = createSpecification(criteria);
        return locationsMapper.toDto(locationsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LocationsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LocationsDTO> findByCriteria(LocationsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Locations> specification = createSpecification(criteria);
        return locationsRepository.findAll(specification, page).map(locationsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocationsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Locations> specification = createSpecification(criteria);
        return locationsRepository.count(specification);
    }

    /**
     * Function to convert {@link LocationsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Locations> createSpecification(LocationsCriteria criteria) {
        Specification<Locations> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Locations_.id));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), Locations_.streetAddress));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), Locations_.postalCode));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Locations_.city));
            }
            if (criteria.getProvince() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProvince(), Locations_.province));
            }
            if (criteria.getCountriesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountriesId(),
                            root -> root.join(Locations_.countries, JoinType.LEFT).get(Countries_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
