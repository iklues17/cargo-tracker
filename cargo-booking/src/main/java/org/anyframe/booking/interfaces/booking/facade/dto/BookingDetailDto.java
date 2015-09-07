package org.anyframe.booking.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.anyframe.booking.infrastructure.tracking.Leg;

public class BookingDetailDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String bookingId;
	private final String userId;
	private final String commodity;
	private final int quantity;
	private final String bookingStatus;
	
	private final String trackingId;
	private final String origin;
	private final String finalDestination;
	private final String arrivalDeadline;
	private final boolean misrouted;
	private final List<Leg> legs;
	private final boolean claimed;
	private final String lastKnownLocation;
	private final String transportStatus;
	private String nextLocation;
	
	
	public BookingDetailDto() {
		this.bookingId = "";
		this.userId = "";
		this.commodity = "";
		this.quantity = 1;
		this.bookingStatus = "";
		this.trackingId = "";
		this.origin = "";
		this.finalDestination = "";
		this.arrivalDeadline = "";
		this.misrouted = false;
		this.legs = new ArrayList<>();
		this.claimed = false;
		this.lastKnownLocation = "";
		this.transportStatus = "";
		this.nextLocation = "";
	}

	public BookingDetailDto(String bookingId, String userId, String commodity,
			int quantity, String bookingStatus, String origin, String finalDestination,
			String arrivalDeadline) {
		this.bookingId = bookingId;
		this.userId = userId;
		this.commodity = commodity;
		this.quantity = quantity;
		this.bookingStatus = bookingStatus;
		this.trackingId = "";
		this.origin = origin;
		this.finalDestination = finalDestination;
		this.arrivalDeadline = arrivalDeadline;
		this.misrouted = false;
		this.legs = new ArrayList<>();
		this.claimed = false;
		this.lastKnownLocation = "";
		this.transportStatus = "";
		this.nextLocation = "";
	};
	
	public BookingDetailDto(String bookingId, String userId, String commodity,
			int quantity, String bookingStatus, String trackingId,
			String origin, String finalDestination, String arrivalDeadline,
			boolean misrouted, boolean claimed,
			String lastKnownLocation, String transportStatus,
			String nextLocation) {
		this.bookingId = bookingId;
		this.userId = userId;
		this.commodity = commodity;
		this.quantity = quantity;
		this.bookingStatus = bookingStatus;
		this.trackingId = trackingId;
		this.origin = origin;
		this.finalDestination = finalDestination;
		this.arrivalDeadline = arrivalDeadline;
		this.misrouted = misrouted;
		this.legs = new ArrayList<>();
		this.claimed = claimed;
		this.lastKnownLocation = lastKnownLocation;
		this.transportStatus = transportStatus;
		this.nextLocation = nextLocation;
	}

	public String getBookingId() {
		return bookingId;
	}

	public String getUserId() {
		return userId;
	}

	public String getCommodity() {
		return commodity;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public String getOrigin() {
		return origin;
	}

	public String getFinalDestination() {
		return finalDestination;
	}

	public String getArrivalDeadline() {
		return arrivalDeadline;
	}

	public boolean isMisrouted() {
		return misrouted;
	}

	public void addLeg(
            String voyageNumber,
            String fromUnLocode, String fromName,
            String toUnLocode, String toName,
            String loadTime, String unloadTime) {
        legs.add(new Leg(voyageNumber,
                fromUnLocode, fromName,
                toUnLocode, toName,
                loadTime, unloadTime));
    }
	
	public List<Leg> getLegs() {
		return legs;
	}

	public boolean isClaimed() {
		return claimed;
	}

	public String getLastKnownLocation() {
		return lastKnownLocation;
	}

	public String getTransportStatus() {
		return transportStatus;
	}

	public String getNextLocation() {
		return nextLocation;
	}

	public void setNextLocation(String nextLocation) {
		this.nextLocation = nextLocation;
	}

	
	
}
