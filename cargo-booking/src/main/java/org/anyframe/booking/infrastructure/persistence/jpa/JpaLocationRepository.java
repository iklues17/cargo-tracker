package org.anyframe.booking.infrastructure.persistence.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.anyframe.booking.domain.location.Location;
import org.anyframe.booking.domain.location.LocationRepository;
import org.anyframe.booking.domain.location.UnLocode;
import org.springframework.stereotype.Repository;

@Repository
public class JpaLocationRepository implements LocationRepository, Serializable {

    private static final long serialVersionUID = 1L;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Location find(UnLocode unLocode) {
        return entityManager.createNamedQuery("Location.findByUnLocode",
                Location.class).setParameter("unLocode", unLocode)
                .getSingleResult();
    }

    @Override
    public List<Location> findAll() {
        return entityManager.createNamedQuery("Location.findAll", Location.class)
                .getResultList();
    }
}
