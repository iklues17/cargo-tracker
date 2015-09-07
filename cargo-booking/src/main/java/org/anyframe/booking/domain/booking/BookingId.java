package org.anyframe.booking.domain.booking;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BookingId implements Serializable{

	private static final long serialVersionUID = 1L;
	@Column(name = "booking_id", unique = true, updatable = false)
	private String id;
	
	public BookingId() {
		
	}
	
	public BookingId(String id) {
		this.id = id;
	}

	public String getIdString() {
		return id;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookingId other = (BookingId) o;

        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    boolean sameValueAs(BookingId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }

}
