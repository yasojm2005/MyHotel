package com.myhotel.company.repository;

import com.myhotel.company.domain.Locations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Locations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationsRepository extends JpaRepository<Locations, Long>, JpaSpecificationExecutor<Locations> {}
