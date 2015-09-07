package org.anyframe.booking.interfaces.booking.web;

import java.util.List;
import java.util.Map;

import org.anyframe.booking.domain.booking.Booking;
import org.anyframe.booking.interfaces.booking.facade.BookingServiceFacade;
import org.anyframe.booking.interfaces.booking.facade.dto.BookingDetailDto;
import org.anyframe.booking.interfaces.booking.facade.dto.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SDS
 *
 */
@RestController
public class BookingController {

	@Autowired
	private BookingServiceFacade bookingServiceFacade;
	
	/**
	 * 특정 유저의 화물운송예약 목록을 조회한다.
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/bookings/of/{userId}", method=RequestMethod.GET)
	public List<BookingDto> listUserBookings(@PathVariable String userId) {
		List<BookingDto> listUserBookings = bookingServiceFacade.listUserBookings(userId);
		return listUserBookings;
	}
	
	/**
	 * 화물운송예약의 상세 정보를 조회한다.
	 * @param bookingId
	 * @return
	 */
	@RequestMapping(value="/bookings/{bookingId}", method=RequestMethod.GET)
	public BookingDetailDto getBookingDetail(@PathVariable String bookingId) {
		BookingDetailDto bookingDetailDto = bookingServiceFacade.getBookingDetail(bookingId);
		return bookingDetailDto;
	}
	
	/**
	 * 승인 이전 상태의 화물운송예약 목록을 조회한다.
	 * @return
	 */
	@RequestMapping(value="/bookings/not-accepted", method=RequestMethod.GET)
	public List<Booking> listNotAcceptedBookings() {
		return bookingServiceFacade.listNotAcceptedBookings();
	}
	
	/**
	 * 화물 운송을 예약한다.
	 * @param newBooking
	 * @return
	 */
	@RequestMapping(value="/bookings", method=RequestMethod.POST)
	public String registerBooking(@RequestBody Map<String,String> newBooking) {
		String originUnlocode = newBooking.get("originUnlocode");
		String destinationUnlocode = newBooking.get("destinationUnlocode");
		
		String bookingId = "";
		if (!originUnlocode.equals(destinationUnlocode)) {
			bookingId = bookingServiceFacade.registerBooking(newBooking);
		} else {
			return null;
		}
		return bookingId;
	}
	
	/**
	 * 승인된 상태로 화물 운송을 예약한다.
	 * @param newBooking
	 * @return
	 */
	@RequestMapping(value="/bookings/accepted-request", method=RequestMethod.POST)
	public String registerAcceptedBooking(@RequestBody Map<String,String> newBooking) {
		String originUnlocode = newBooking.get("originUnlocode");
		String destinationUnlocode = newBooking.get("destinationUnlocode");
		String bookingId = "";
		if (!originUnlocode.equals(destinationUnlocode)) {
			bookingId = bookingServiceFacade.registerAcceptedBooking(newBooking);
		} else {
			return null;
		}
		
		return bookingId;
	}
	
	/**
	 * 화물 운송 예약을 승인한다.
	 * @param bookingId
	 */
	@RequestMapping(value="/bookings/{bookingId}/accept", method=RequestMethod.POST)
	public void acceptBooking(@PathVariable String bookingId) {
		bookingServiceFacade.acceptBooking(bookingId);
	}
	
	/**
	 * 화물 운송 목적지를 변경한다.
	 * @param bookingId
	 */
	@RequestMapping(value="/bookings/{bookingId}/change-destination", method=RequestMethod.PUT)
	public void changeDestination(@RequestBody  Map<String,String> destination, @PathVariable String bookingId) {
		bookingServiceFacade.changeDestination(bookingId, destination.get("destinationUnlocode"));
	}
	
	/**
	 * 화물 운송 상태를 조회한다.
	 * @param
	 */
//	@RequestMapping(value="/trackings/{trackingId}", method=RequestMethod.GET)
//	public CargoTrackingViewDto onTrackById(@PathVariable String trackingId) {
//		CargoTrackingViewDto cargoTrackingViewDto = bookingServiceFacade.getBookingDetail(trackingId);
//		return cargoTrackingViewDto;
//	}
}
