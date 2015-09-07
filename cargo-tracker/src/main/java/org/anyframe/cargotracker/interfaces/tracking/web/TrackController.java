package org.anyframe.cargotracker.interfaces.tracking.web;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.anyframe.cargotracker.domain.model.cargo.Cargo;
import org.anyframe.cargotracker.domain.model.cargo.CargoRepository;
import org.anyframe.cargotracker.domain.model.cargo.TrackingId;
import org.anyframe.cargotracker.domain.model.handling.HandlingEvent;
import org.anyframe.cargotracker.domain.model.handling.HandlingEventRepository;
import org.anyframe.cargotracker.interfaces.tracking.dto.CargoTrackingViewDto;
import org.anyframe.cargotracker.interfaces.tracking.internal.assembler.CargoTrackingViewAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/**
 * Backing bean for tracking cargo. This interface sits immediately on top of
 * the domain layer, unlike the booking interface which has a facade and
 * supporting DTOs in between.
 * <p/>
 * An adapter class, designed for the tracking use case, is used to wrap the
 * domain model to make it easier to work with in a web page rendering context.
 * We do not want to apply view rendering constraints to the design of our
 * domain model, and the adapter helps us shield the domain model classes.
 * <p/>
 * In some very simplistic cases, it may be fine to not use even an adapter.
 */

@RestController
public class TrackController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final Logger logger = LoggerFactory.getLogger(TrackController.class);

    @Inject
    private CargoRepository cargoRepository;
    @Inject
    private HandlingEventRepository handlingEventRepository;

	@RequestMapping("/cargos/{trackingId}/tracks")
	@HystrixCommand(fallbackMethod = "trackingFallback"
	, commandProperties = { @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "1")})
    public CargoTrackingViewDto onTrackById(@PathVariable String trackingId) {
        Cargo cargo = cargoRepository.find(new TrackingId(trackingId));

        if (cargo != null) {
        	
        	if("JKL567".equals(trackingId)){
        		throw new RuntimeException("##### Hystrix Test!!!!");
        	}
        	else {
        		 List<HandlingEvent> handlingEvents = handlingEventRepository
                         .lookupHandlingHistoryOfCargo(new TrackingId(trackingId))
                         .getDistinctEventsByCompletionTime();
                 
                 CargoTrackingViewAssembler assembler = new CargoTrackingViewAssembler(cargo, handlingEvents);
                 
                 return assembler.toDto();
        	}
        } 
        else {
        	return null;
        }
    }
    
    public CargoTrackingViewDto trackingFallback(String trackingId) {

    	logger.debug("Tracking Id is {} This is fallback Method !!!!!!!!!!!!!!!", trackingId);

    	Cargo cargo = cargoRepository.find(new TrackingId(trackingId));
    	
    	if (cargo != null) {
    		List<HandlingEvent> handlingEvents = handlingEventRepository
                 .lookupHandlingHistoryOfCargo(new TrackingId(trackingId))
                 .getDistinctEventsByCompletionTime();
         
    		CargoTrackingViewAssembler assembler = new CargoTrackingViewAssembler(cargo, handlingEvents);
         
    		return assembler.toDto();
    	}
    	else {
    		return null;
    	}
    }
}