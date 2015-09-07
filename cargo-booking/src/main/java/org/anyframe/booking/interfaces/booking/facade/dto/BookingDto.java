package org.anyframe.booking.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
    
	private final String bookingId;
	private final String trackingId;
	private final String origin;
	private final String destination;
	private final String arrivalDeadline;
	private final String userId;
	private final String commodity;
	private final int quantity;
	private final String bookingStatus;
	
	public BookingDto() {
		this.bookingId = "";
		this.trackingId = "";
		this.origin = "";
		this.destination = "";
		this.arrivalDeadline = "";
		this.userId = "";
		this.commodity = "";
		this.quantity = 1;
		this.bookingStatus = "";
	}

	public BookingDto(String bookingId, String trackingId, String origin,
			String destination, Date arrivalDeadline, String userId,
			String commodity, int quantity, String bookingStatus) {
		this.bookingId = bookingId;
		this.trackingId = trackingId;
		this.origin = origin;
		this.destination = destination;
		this.arrivalDeadline = DATE_FORMAT.format(arrivalDeadline);;
		this.userId = userId;
		this.commodity = commodity;
		this.quantity = quantity;
		this.bookingStatus = bookingStatus;
	}

	public String getBookingId() {
		return bookingId;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDestination() {
		return destination;
	}

	public String getArrivalDeadline() {
		return arrivalDeadline;
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

	
}
