package com.wedevzone.digiparc.repository;

import com.wedevzone.digiparc.domain.Formation;
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
public class FormationRepositoryWithBagRelationshipsImpl implements FormationRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Formation> fetchBagRelationships(Optional<Formation> formation) {
        return formation.map(this::fetchSubscribers);
    }

    @Override
    public Page<Formation> fetchBagRelationships(Page<Formation> formations) {
        return new PageImpl<>(fetchBagRelationships(formations.getContent()), formations.getPageable(), formations.getTotalElements());
    }

    @Override
    public List<Formation> fetchBagRelationships(List<Formation> formations) {
        return Optional.of(formations).map(this::fetchSubscribers).orElse(Collections.emptyList());
    }

    Formation fetchSubscribers(Formation result) {
        return entityManager
            .createQuery(
                "select formation from Formation formation left join fetch formation.subscribers where formation is :formation",
                Formation.class
            )
            .setParameter("formation", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Formation> fetchSubscribers(List<Formation> formations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, formations.size()).forEach(index -> order.put(formations.get(index).getId(), index));
        List<Formation> result = entityManager
            .createQuery(
                "select distinct formation from Formation formation left join fetch formation.subscribers where formation in :formations",
                Formation.class
            )
            .setParameter("formations", formations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
