package com.wedevzone.digiparc.repository;

import com.wedevzone.digiparc.domain.Subscriber;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Subscriber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {}
