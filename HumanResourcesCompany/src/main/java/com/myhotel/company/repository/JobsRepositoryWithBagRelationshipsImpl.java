package com.myhotel.company.repository;

import com.myhotel.company.domain.Jobs;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class JobsRepositoryWithBagRelationshipsImpl implements JobsRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Jobs> fetchBagRelationships(Optional<Jobs> jobs) {
        return jobs.map(this::fetchJobHistories);
    }

    @Override
    public Page<Jobs> fetchBagRelationships(Page<Jobs> jobs) {
        return new PageImpl<>(fetchBagRelationships(jobs.getContent()), jobs.getPageable(), jobs.getTotalElements());
    }

    @Override
    public List<Jobs> fetchBagRelationships(List<Jobs> jobs) {
        return Optional.of(jobs).map(this::fetchJobHistories).orElse(Collections.emptyList());
    }

    Jobs fetchJobHistories(Jobs result) {
        return entityManager
            .createQuery("select jobs from Jobs jobs left join fetch jobs.jobHistories where jobs is :jobs", Jobs.class)
            .setParameter("jobs", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Jobs> fetchJobHistories(List<Jobs> jobs) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, jobs.size()).forEach(index -> order.put(jobs.get(index).getId(), index));
        List<Jobs> result = entityManager
            .createQuery("select distinct jobs from Jobs jobs left join fetch jobs.jobHistories where jobs in :jobs", Jobs.class)
            .setParameter("jobs", jobs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
