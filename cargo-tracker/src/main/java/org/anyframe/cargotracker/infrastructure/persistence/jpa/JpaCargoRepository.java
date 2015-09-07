package org.anyframe.cargotracker.infrastructure.persistence.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.anyframe.cargotracker.domain.model.cargo.Cargo;
import org.anyframe.cargotracker.domain.model.cargo.CargoRepository;
import org.anyframe.cargotracker.domain.model.cargo.Leg;
import org.anyframe.cargotracker.domain.model.cargo.TrackingId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class JpaCargoRepository implements CargoRepository, Serializable {

    private static final long serialVersionUID = 1L;

    private final Logger logger = LoggerFactory.getLogger(
            JpaCargoRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Cargo find(TrackingId trackingId) {
        Cargo cargo;

        try {
            cargo = entityManager.createNamedQuery("Cargo.findByTrackingId",
                    Cargo.class)
                    .setParameter("trackingId", trackingId)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.debug("Find called on non-existant tracking ID.", e);
            cargo = null;
        }

        return cargo;
    }

    @Override
    public void store(Cargo cargo) {
        // TODO See why cascade is not working correctly for legs.
        for (Leg leg : cargo.getItinerary().getLegs()) {
            entityManager.persist(leg);
        }

        entityManager.persist(cargo);
    }

    @Override
    public TrackingId nextTrackingId() {
        String random = UUID.randomUUID().toString().toUpperCase();

        return new TrackingId(random.substring(0, random.indexOf("-")));
    }

    @Override
    public List<Cargo> findAll() {
        return entityManager.createNamedQuery("Cargo.findAll", Cargo.class)
                .getResultList();
    }
}
