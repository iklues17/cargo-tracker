package org.anyframe.booking.interfaces.booking.facade.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.anyframe.booking.application.BookingService;
import org.anyframe.booking.domain.booking.Booking;
import org.anyframe.booking.domain.booking.BookingId;
import org.anyframe.booking.domain.booking.BookingRepository;
import org.anyframe.booking.domain.booking.BookingStatus;
import org.anyframe.booking.domain.location.Location;
import org.anyframe.booking.domain.location.LocationRepository;
import org.anyframe.booking.domain.location.UnLocode;
import org.anyframe.booking.infrastructure.tracking.CargoRoute;
import org.anyframe.booking.infrastructure.tracking.CargoTrackerService;
import org.anyframe.booking.interfaces.booking.facade.BookingServiceFacade;
import org.anyframe.booking.interfaces.booking.facade.dto.BookingDetailDto;
import org.anyframe.booking.interfaces.booking.facade.dto.BookingDto;
import org.anyframe.booking.interfaces.booking.facade.internal.assembler.BookingDetailDtoAssembler;
import org.anyframe.booking.interfaces.booking.facade.internal.assembler.BookingDtoAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class DefaultBookingServiceFacade implements BookingServiceFacade,Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
    private BookingRepository bookingRepository;

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private CargoTrackerService cargoTrackerService;
	
	@Autowired 
	private RestTemplate restTemplate;
	
	@Override
	public List<BookingDto> listUserBookings(String userId) {
		List<Booking> bookings = bookingRepository.findByUserId(userId); 
		List<BookingDto> bookingList = new ArrayList<>(bookings.size());
		
		BookingDtoAssembler assembler = new BookingDtoAssembler();
		for (Booking booking : bookings) {
			bookingList.add(assembler.toDto(booking));
		}
		return bookingList;
	}

	@Override
	public BookingDetailDto getBookingDetail(String bookingId) {
		
		Booking booking = bookingRepository.findByBookingId(new BookingId(bookingId));

		String trackingId = booking.getTrackingId();		
		
		BookingDetailDto bookingDetailDto = null;
		
		if (BookingStatus.ACCEPTED.equals(booking.getBookingStatus())) {
			CargoRoute cargoRoute = cargoTrackerService.getCargo(trackingId);
			bookingDetailDto = new BookingDetailDtoAssembler().toDto(booking, cargoRoute);
		}
		else {
			bookingDetailDto = new BookingDetailDtoAssembler().toDto(booking);
		}
		
		return bookingDetailDto;
	}

	@Override
	public List<Booking> listNotAcceptedBookings() {
		BookingStatus bookingStatus = BookingStatus.NOT_ACCEPTED;
		return bookingRepository.findByBookingStatus(bookingStatus);
	}

	@Override
	public String registerBooking(Map<String,String> bookingInformation) {
		BookingId bookingId = bookingService.registerBooking(bookingInformation, BookingStatus.NOT_ACCEPTED);
		
		return bookingId.getIdString();
	}

	@Override
	public String registerAcceptedBooking(Map<String,String> bookingInformation) {
		BookingId bookingId = bookingService.registerBooking(bookingInformation, BookingStatus.ACCEPTED);
		
		return bookingId.getIdString();	
	}

	@Override
	public void acceptBooking(String bookingId) {
		bookingService.acceptBooking(new BookingId(bookingId));
	}


	@Override
	public String changeDestination(String bookingId, String destination) {
		bookingService.changeDestination(new BookingId(bookingId), destination);
		return null;
	}


}
