package com.myhotel.company.service;

import com.myhotel.company.domain.*; // for static metamodels
import com.myhotel.company.domain.Departments;
import com.myhotel.company.repository.DepartmentsRepository;
import com.myhotel.company.service.criteria.DepartmentsCriteria;
import com.myhotel.company.service.dto.DepartmentsDTO;
import com.myhotel.company.service.mapper.DepartmentsMapper;
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
 * Service for executing complex queries for {@link Departments} entities in the database.
 * The main input is a {@link DepartmentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepartmentsDTO} or a {@link Page} of {@link DepartmentsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepartmentsQueryService extends QueryService<Departments> {

    private final Logger log = LoggerFactory.getLogger(DepartmentsQueryService.class);

    private final DepartmentsRepository departmentsRepository;

    private final DepartmentsMapper departmentsMapper;

    public DepartmentsQueryService(DepartmentsRepository departmentsRepository, DepartmentsMapper departmentsMapper) {
        this.departmentsRepository = departmentsRepository;
        this.departmentsMapper = departmentsMapper;
    }

    /**
     * Return a {@link List} of {@link DepartmentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepartmentsDTO> findByCriteria(DepartmentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Departments> specification = createSpecification(criteria);
        return departmentsMapper.toDto(departmentsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepartmentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepartmentsDTO> findByCriteria(DepartmentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Departments> specification = createSpecification(criteria);
        return departmentsRepository.findAll(specification, page).map(departmentsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepartmentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Departments> specification = createSpecification(criteria);
        return departmentsRepository.count(specification);
    }

    /**
     * Function to convert {@link DepartmentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Departments> createSpecification(DepartmentsCriteria criteria) {
        Specification<Departments> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Departments_.id));
            }
            if (criteria.getDepartmentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepartmentName(), Departments_.departmentName));
            }
            if (criteria.getDeparmentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeparmentType(), Departments_.deparmentType));
            }
            if (criteria.getLocationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationsId(),
                            root -> root.join(Departments_.locations, JoinType.LEFT).get(Locations_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
