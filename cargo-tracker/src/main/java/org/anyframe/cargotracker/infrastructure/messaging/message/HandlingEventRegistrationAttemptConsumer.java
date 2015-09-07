package org.anyframe.cargotracker.infrastructure.messaging.message;

import org.anyframe.cargotracker.application.HandlingEventService;
import org.anyframe.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import org.anyframe.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

/**
 * Consumes handling event registration attempt messages and delegates to proper
 * registration.
 */
public class HandlingEventRegistrationAttemptConsumer {

	private final Logger logger = LoggerFactory.getLogger(HandlingEventRegistrationAttemptConsumer.class);
	
	private HandlingEventService handlingEventService;
   
	public void handleMessage(HandlingEventRegistrationAttempt attempt) {
		try {
			logger.debug("## This is HandlingEventRegistrationAttemptConsumer Received TrackingID: {} ##", attempt.getTrackingId());
			
			if(handlingEventService == null) {
				logger.debug("## Getting the defaultHandlingEventService bean ##");
				handlingEventService = (HandlingEventService)ApplicationContextProvider.getApplicationContext().getBean("defaultHandlingEventService");
			}
			
			handlingEventService.registerHandlingEvent(
					attempt.getCompletionTime(), attempt.getTrackingId(),
					attempt.getVoyageNumber(), attempt.getUnLocode(),
					attempt.getType());
		} catch (CannotCreateHandlingEventException e) {
			 throw new AmqpRejectAndDontRequeueException("Error occurred processing message", e);
		} catch (Exception ex) {
			 throw new RuntimeException("Error occurred processing message", ex);
		}
	}
    
}
