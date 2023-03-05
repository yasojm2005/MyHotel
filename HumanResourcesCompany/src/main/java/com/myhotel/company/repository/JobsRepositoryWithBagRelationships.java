package com.myhotel.company.repository;

import com.myhotel.company.domain.Jobs;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface JobsRepositoryWithBagRelationships {
    Optional<Jobs> fetchBagRelationships(Optional<Jobs> jobs);

    List<Jobs> fetchBagRelationships(List<Jobs> jobs);

    Page<Jobs> fetchBagRelationships(Page<Jobs> jobs);
}
