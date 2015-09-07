package org.anyframe.cargotracker.infrastructure.persistence.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.anyframe.cargotracker.domain.model.voyage.Voyage;
import org.anyframe.cargotracker.domain.model.voyage.VoyageNumber;
import org.anyframe.cargotracker.domain.model.voyage.VoyageRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaVoyageRepository implements VoyageRepository, Serializable {

	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Voyage find(VoyageNumber voyageNumber) {
		return entityManager
				.createNamedQuery("Voyage.findByVoyageNumber", Voyage.class)
				.setParameter("voyageNumber", voyageNumber).getSingleResult();
	}
}
