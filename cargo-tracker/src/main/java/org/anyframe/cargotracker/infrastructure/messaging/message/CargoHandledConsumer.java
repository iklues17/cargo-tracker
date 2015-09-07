package org.anyframe.cargotracker.infrastructure.messaging.message;

import org.anyframe.cargotracker.application.CargoInspectionService;
import org.anyframe.cargotracker.domain.model.cargo.TrackingId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumes JMS messages and delegates notification of misdirected cargo to the
 * tracking service.
 * 
 * This is a programmatic hook into the JMS infrastructure to make cargo
 * inspection message-driven.
 */
public class CargoHandledConsumer {

	private final Logger logger = LoggerFactory.getLogger(CargoHandledConsumer.class);
	
	private CargoInspectionService cargoInspectionService;

	public void handleMessage(String trackingIdString) {
		try {
			if(cargoInspectionService == null) {
				logger.debug("## Getting the defaultCargoInspectionService bean ##");
				cargoInspectionService = (CargoInspectionService)ApplicationContextProvider.getApplicationContext().getBean("defaultCargoInspectionService");
			}
			
			cargoInspectionService.inspectCargo(new TrackingId(trackingIdString));
		} catch (Exception e) {
			logger.debug("Error procesing JMS message", e);
		}
	}
	
}
