package org.anyframe.booking.interfaces.booking.facade.internal.assembler;

import java.text.SimpleDateFormat;
import java.util.List;

import org.anyframe.booking.domain.booking.Booking;
import org.anyframe.booking.domain.location.Location;
import org.anyframe.booking.infrastructure.tracking.CargoRoute;
import org.anyframe.booking.infrastructure.tracking.Leg;
import org.anyframe.booking.interfaces.booking.facade.dto.BookingDetailDto;

public class BookingDetailDtoAssembler {
	
	
	private static final SimpleDateFormat DATE_FORMAT
    = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

	public BookingDetailDtoAssembler() {
	}
	public BookingDetailDto toDto(Booking booking) {
		
		BookingDetailDto dto = new BookingDetailDto(
				booking.getBookingId().getIdString(),
				booking.getUserId(),
				booking.getCommodity(),
				booking.getQuantity(),
				booking.getBookingStatus().name(),
				booking.getOrigin().getName() + " (" + booking.getOrigin().getUnLocode().getIdString() + ")",
				booking.getDestination().getName() + " (" + booking.getDestination().getUnLocode().getIdString() + ")",
				DATE_FORMAT.format(booking.getArrivalDeadline())
		);
		return dto;
	}
	public BookingDetailDto toDto(Booking booking, CargoRoute cargoRoute) {
		
		BookingDetailDto dto = new BookingDetailDto(
				booking.getBookingId().getIdString(),
				booking.getUserId(),
				booking.getCommodity(),
				booking.getQuantity(),
				booking.getBookingStatus().name(),
				cargoRoute.getTrackingId(),
				cargoRoute.getOrigin(),
				cargoRoute.getFinalDestination(),
				cargoRoute.getArrivalDeadline(),
				cargoRoute.isMisrouted(),
				cargoRoute.isClaimed(),
				cargoRoute.getLastKnownLocation(),
				cargoRoute.getTransportStatus(),
				cargoRoute.getNextLocation()
		);
		for (Leg leg : cargoRoute.getLegs()) {
			
			dto.addLeg(leg.getVoyageNumber(),
					booking.getOrigin().getUnLocode().getIdString(),
					booking.getOrigin().getName(), 
					booking.getDestination().getUnLocode().getIdString(),
					booking.getDestination().getName(), 
					leg.getLoadTime(),
					leg.getUnloadTime());
        }
		return dto;
	}

}
