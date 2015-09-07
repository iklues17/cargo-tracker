package org.anyframe.cargotracker.interfaces.booking.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anyframe.cargotracker.domain.model.cargo.Cargo;
import org.anyframe.cargotracker.domain.model.cargo.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CargoMonitoringService {

    @Autowired
    private CargoRepository cargoRepository;

    @RequestMapping(value="/cargo", method=RequestMethod.GET)
    public List<Map<String, Object>> getAllCargo() {
		List<Cargo> cargos = cargoRepository.findAll();
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (Cargo cargo : cargos) {
			 Map<String, Object> map = new HashMap<String, Object>();
			 
			 map.put("trackingId", cargo.getTrackingId().getIdString());
			 map.put("routingStatus", cargo.getDelivery().getRoutingStatus().toString());
			 map.put("misdirected", cargo.getDelivery().isMisdirected());
			 map.put("transportStatus", cargo.getDelivery().getTransportStatus().toString());
			 map.put("atDestination", cargo.getDelivery().isUnloadedAtDestination());
			 map.put("origin", cargo.getOrigin().getUnLocode().getIdString());
			 map.put("lastKnownLocation",cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString().equals("XXXXX")? "Unknown": cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString());
			 
			 list.add(map);
		 }
		
    	return list;
    }
    
}
