package org.anyframe.booking.application.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.anyframe.booking.application.BookingService;
import org.anyframe.booking.domain.booking.Booking;
import org.anyframe.booking.domain.booking.BookingId;
import org.anyframe.booking.domain.booking.BookingRepository;
import org.anyframe.booking.domain.booking.BookingStatus;
import org.anyframe.booking.domain.location.Location;
import org.anyframe.booking.domain.location.LocationRepository;
import org.anyframe.booking.domain.location.UnLocode;
import org.anyframe.booking.infrastructure.tracking.CargoTrackerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DefaultBookingService implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CargoTrackerService cargoTrackerService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(
			 DefaultBookingService.class);
	 
	@Override
	public BookingId registerBooking(Map<String, String> bookingInformation, BookingStatus bookingStatus) {
		BookingId bookingId = bookingRepository.nextBookingId();
		
		String trackingId = "";
		Date arrivalDeadline = null;
		
		Location origin = locationRepository.find(new UnLocode(bookingInformation.get("originUnlocode")));
		Location destination = locationRepository.find(new UnLocode(bookingInformation.get("destinationUnlocode")));
		try {
			arrivalDeadline = new SimpleDateFormat("yyyy-MM-dd")
					.parse(bookingInformation.get("arrivalDeadline"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 관리자 예약
		if (BookingStatus.ACCEPTED.equals(bookingStatus)) {
			trackingId = cargoTrackerService.registerCargo(bookingInformation);
			logger.debug("CargoTracker booked new cargo, tracking id {}", trackingId);
		} 
		
		Booking booking = new Booking(bookingId,
				trackingId,
				origin,
				destination,
				arrivalDeadline,
				bookingInformation.get("userId"),
				bookingInformation.get("commodity"),
				Integer.parseInt(bookingInformation.get("quantity")),
				bookingStatus);
		bookingRepository.store(booking);

        logger.debug("Registered new booking with booking id {}",
                booking.getBookingId().getIdString());
        
		return booking.getBookingId();
	}

	@Override
	public void acceptBooking(BookingId bookingId) {
		Booking booking = bookingRepository.findByBookingId(bookingId);
		
		String trackingId = "";
		
		Map<String, String> cargoBookingParameters = new HashMap<String, String>();
		
		cargoBookingParameters.put("originUnlocode", booking.getOrigin().getUnLocode().getIdString());
		cargoBookingParameters.put("destinationUnlocode", booking.getDestination().getUnLocode().getIdString());
		cargoBookingParameters.put("arrivalDeadline", booking.getArrivalDeadline().toString());
		
		trackingId = cargoTrackerService.registerCargo(cargoBookingParameters);
		logger.debug("CargoTracker booked new cargo, it's tracking id {}", trackingId);
		
		booking.setTrackingId(trackingId);
		booking.setBookingStatus(BookingStatus.ACCEPTED);
		
		bookingRepository.store(booking);
		
	}

	@Override
	public String changeDestination(BookingId bookingId,
			String destination) {
		Booking booking = bookingRepository.findByBookingId(bookingId);
		
		Location newDestination = locationRepository.find(new UnLocode(destination));
		booking.setDestinationId(newDestination);
		
		bookingRepository.store(booking);
		
		// 승인된 상태라면 관리자만 변경 가능
		// 관리자가 변경할 경우엔 변경된 목적지 정보 CargoTracker로 퍼블리시
		if (BookingStatus.ACCEPTED.equals(booking.getBookingStatus())) {
			cargoTrackerService.changeDestination(booking.getTrackingId(), destination);
		}
		
		return booking.getBookingId().getIdString();
	}

}
