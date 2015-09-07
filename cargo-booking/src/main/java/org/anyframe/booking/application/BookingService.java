package org.anyframe.booking.application;

import java.util.Map;

import org.anyframe.booking.domain.booking.BookingId;
import org.anyframe.booking.domain.booking.BookingStatus;

public interface BookingService {

	BookingId registerBooking(Map<String, String> bookingInformation, BookingStatus bookingStatus);
	
	void acceptBooking(BookingId bookingId);

	String changeDestination(BookingId bookingId, String destination);
}
