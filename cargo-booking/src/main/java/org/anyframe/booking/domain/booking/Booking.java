package org.anyframe.booking.domain.booking;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import org.anyframe.booking.domain.location.Location;

@Entity
@NamedQueries({
		@NamedQuery(name = "Booking.findAll", query = "Select b from Booking b"),
		@NamedQuery(name = "Booking.findByUserId", query = "Select b from Booking b where b.userId = :userId"),
		@NamedQuery(name = "Booking.findByBookingStatus", query = "Select b from Booking b where b.bookingStatus = :bookingStatus"),
		@NamedQuery(name = "Booking.findByBookingId", query = "Select b from Booking b where b.bookingId = :bookingId") })
public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Embedded
	private BookingId bookingId;

	@Column(name = "tracking_id")
	private String trackingId;

	@ManyToOne
    @JoinColumn(name = "origin_id", updatable = false)
	private Location origin;
	@ManyToOne
    @JoinColumn(name = "destination_id", updatable = false)
	private Location destination;

	@Column(name = "arrival_deadline")
	private Date arrivalDeadline;

	@Column(name = "user_id")
	private String userId;

	private String commodity;

	private int quantity;

	@Enumerated(EnumType.STRING)
	@Column(name = "booking_status")
	@NotNull
	private BookingStatus bookingStatus;

	public Booking() {
		
	}
	
	public Booking(BookingId bookingId, String trackingId,
			Location origin, Location destination, Date arrivalDeadline,
			String userId, String commodity, int quantity,
			BookingStatus bookingStatus) {
		super();
		this.bookingId = bookingId;
		this.trackingId = trackingId;
		this.origin = origin;
		this.destination = destination;
		this.arrivalDeadline = arrivalDeadline;
		this.userId = userId;
		this.commodity = commodity;
		this.quantity = quantity;
		this.bookingStatus = bookingStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BookingId getBookingId() {
		return bookingId;
	}

	public void setBookingId(BookingId bookingId) {
		this.bookingId = bookingId;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public Location getOrigin() {
		return origin;
	}

	public Location getDestination() {
		return destination;
	}

	public void setOriginId(Location origin) {
		this.origin = origin;
	}

	public void setDestinationId(Location destination) {
		this.destination = destination;
	}

	public Date getArrivalDeadline() {
		return arrivalDeadline;
	}

	public void setArrivalDeadline(Date arrivalDeadline) {
		this.arrivalDeadline = arrivalDeadline;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCommodity() {
		return commodity;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}

		Booking other = (Booking) object;
		return sameIdentityAs(other);
	}

	private boolean sameIdentityAs(Booking other) {
		return other != null && bookingId.sameValueAs(other.bookingId);
	}

	@Override
	public int hashCode() {
		return bookingId.hashCode();
	}

	@Override
	public String toString() {
		return bookingId.toString();
	}

}
