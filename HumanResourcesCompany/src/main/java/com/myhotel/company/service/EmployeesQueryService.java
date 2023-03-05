package com.myhotel.company.service;

import com.myhotel.company.domain.*; // for static metamodels
import com.myhotel.company.domain.Employees;
import com.myhotel.company.repository.EmployeesRepository;
import com.myhotel.company.service.criteria.EmployeesCriteria;
import com.myhotel.company.service.dto.EmployeesDTO;
import com.myhotel.company.service.mapper.EmployeesMapper;
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
 * Service for executing complex queries for {@link Employees} entities in the database.
 * The main input is a {@link EmployeesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeesDTO} or a {@link Page} of {@link EmployeesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeesQueryService extends QueryService<Employees> {

    private final Logger log = LoggerFactory.getLogger(EmployeesQueryService.class);

    private final EmployeesRepository employeesRepository;

    private final EmployeesMapper employeesMapper;

    public EmployeesQueryService(EmployeesRepository employeesRepository, EmployeesMapper employeesMapper) {
        this.employeesRepository = employeesRepository;
        this.employeesMapper = employeesMapper;
    }

    /**
     * Return a {@link List} of {@link EmployeesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeesDTO> findByCriteria(EmployeesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Employees> specification = createSpecification(criteria);
        return employeesMapper.toDto(employeesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployeesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeesDTO> findByCriteria(EmployeesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employees> specification = createSpecification(criteria);
        return employeesRepository.findAll(specification, page).map(employeesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Employees> specification = createSpecification(criteria);
        return employeesRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Employees> createSpecification(EmployeesCriteria criteria) {
        Specification<Employees> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Employees_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Employees_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Employees_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Employees_.email));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Employees_.phoneNumber));
            }
            if (criteria.getHireDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHireDate(), Employees_.hireDate));
            }
            if (criteria.getSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalary(), Employees_.salary));
            }
            if (criteria.getCommisionPCT() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommisionPCT(), Employees_.commisionPCT));
            }
            if (criteria.getDepartmentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentsId(),
                            root -> root.join(Employees_.departments, JoinType.LEFT).get(Departments_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
