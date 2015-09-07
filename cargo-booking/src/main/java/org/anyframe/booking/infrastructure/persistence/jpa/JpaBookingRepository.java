package org.anyframe.booking.infrastructure.persistence.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.anyframe.booking.domain.booking.Booking;
import org.anyframe.booking.domain.booking.BookingId;
import org.anyframe.booking.domain.booking.BookingRepository;
import org.anyframe.booking.domain.booking.BookingStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class JpaBookingRepository implements BookingRepository, Serializable {

	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory
			.getLogger(JpaBookingRepository.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Booking> findAll() {
		return entityManager
				.createNamedQuery("Booking.findAll", Booking.class).getResultList();
	}

	@Override
	public void store(Booking booking) {
		entityManager.persist(booking);
	}

	@Override
	public BookingId nextBookingId() {
		String random = UUID.randomUUID().toString().toUpperCase();

        return new BookingId(random.substring(0, random.indexOf("-")));
    }

	@Override
	public Booking findByBookingId(BookingId bookingId) {
		return entityManager
				.createNamedQuery("Booking.findByBookingId", Booking.class)
				.setParameter("bookingId", bookingId).getSingleResult();
	}

	@Override
	public List<Booking> findByUserId(String userId) {
		return entityManager
				.createNamedQuery("Booking.findByUserId", Booking.class)
				.setParameter("userId", userId).getResultList();
	}

	@Override
	public List<Booking> findByBookingStatus(BookingStatus bookingStatus) {
		return entityManager
				.createNamedQuery("Booking.findByBookingStatus", Booking.class)
				.setParameter("bookingStatus", bookingStatus).getResultList();
	}

}
