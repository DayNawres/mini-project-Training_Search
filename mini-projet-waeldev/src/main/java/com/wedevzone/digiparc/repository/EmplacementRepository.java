package com.wedevzone.digiparc.repository;

import com.wedevzone.digiparc.domain.Emplacement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Emplacement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmplacementRepository extends JpaRepository<Emplacement, Long> {}
