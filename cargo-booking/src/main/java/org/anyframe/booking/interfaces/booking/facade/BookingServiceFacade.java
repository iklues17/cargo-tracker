package org.anyframe.booking.interfaces.booking.facade;

import java.util.List;
import java.util.Map;

import org.anyframe.booking.domain.booking.Booking;
import org.anyframe.booking.interfaces.booking.facade.dto.BookingDetailDto;
import org.anyframe.booking.interfaces.booking.facade.dto.BookingDto;

public interface BookingServiceFacade {

	List<BookingDto> listUserBookings(String userId);
	
	BookingDetailDto getBookingDetail(String bookingId);
	
	List<Booking> listNotAcceptedBookings();
	
	String registerBooking(Map<String,String> bookingInfomation);

	String registerAcceptedBooking(Map<String, String> bookingInfomation);	
	
	void acceptBooking(String bookingId);
	
	String changeDestination(String bookingId, String destination);

}
