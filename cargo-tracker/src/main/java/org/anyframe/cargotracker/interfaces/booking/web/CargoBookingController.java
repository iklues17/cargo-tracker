package org.anyframe.cargotracker.interfaces.booking.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.anyframe.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.anyframe.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.anyframe.cargotracker.interfaces.booking.facade.dto.Leg;
import org.anyframe.cargotracker.interfaces.booking.facade.dto.RouteCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SDS
 *
 */
@RestController
public class CargoBookingController {

	@Autowired
	private BookingServiceFacade bookingServiceFacade;
	
	/**
	 * Cargo 전체 목록을 조회한다. 
	 * @return
	 */
	@RequestMapping(value="/cargos", method=RequestMethod.GET)
	public List<CargoRoute> listAllCargos() {
		List<CargoRoute> listAllCargos = bookingServiceFacade.listAllCargos();
		return listAllCargos;
	}

	/**
	 * Cargo 를 등록하고 Show 페이지로 이동한다. 
	 * @param originUnlocode
	 * @param destinationUnlocode
	 * @param arrivalDeadline
	 * @return
	 */
	@RequestMapping(value="/cargos/registration", method=RequestMethod.POST)
	public String registerCargo(@RequestBody  Map<String,String> newCargo) {
		String originUnlocode = newCargo.get("originUnlocode");
		String destinationUnlocode = newCargo.get("destinationUnlocode");
		String arrivalDeadline = newCargo.get("arrivalDeadline");
		String trackingId = "";
		
		try {
			if (!originUnlocode.equals(destinationUnlocode)) {
				trackingId = bookingServiceFacade.bookNewCargo(originUnlocode, destinationUnlocode,	new SimpleDateFormat("yyyy-MM-dd").parse(arrivalDeadline));
			} else {
				return null;
			}
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}
		//return "show.html?trackingId=" + trackingId;
		return trackingId;
	}
	
	/**
	 * Cargo 의 Detail 정보를 가져온다.
	 * @param trackingId
	 * @return
	 */
	@RequestMapping("/cargos/{trackingId}")
	public CargoRoute getCargo(@PathVariable String trackingId) {
		return bookingServiceFacade.loadCargoForRouting(trackingId);
	}

	
	@RequestMapping("/cargos/candidates")
	public List<RouteCandidate> getRouteCanditates(@RequestParam String trackingId) {
		return bookingServiceFacade.requestPossibleRoutesForCargo(trackingId);
	}

	/**
	 * 경로를 Assing 하고 Show 페이지로 이동한다. 
	 * @param legs
	 * @param trackingId
	 * @return
	 */
	@RequestMapping(value="/cargos/{trackingId}/itinerary-assign-to" , method=RequestMethod.POST)
	public String assignItinerary(@RequestBody List<Leg> legs, @PathVariable String trackingId) {
		RouteCandidate selectedRoute = new RouteCandidate(legs);
		bookingServiceFacade.assignCargoToRoute(trackingId, selectedRoute);

		return trackingId;
	}
	
	/**
	 * 목적지를 변경한다. 
	 * @param trackingId
	 * @param destinationUnlocode
	 * @return
	 */
	@RequestMapping(value="/cargos/{trackingId}/change-destination", method=RequestMethod.POST)
	public String changeDestination(@RequestBody  Map<String,String> destination, @PathVariable String trackingId) {
		bookingServiceFacade.changeDestination(trackingId, destination.get("destinationUnlocode"));

		// CargoAdmin 파일에 있는 changeDestination
		return "show.html?trackingId=" + trackingId;
	}
}
