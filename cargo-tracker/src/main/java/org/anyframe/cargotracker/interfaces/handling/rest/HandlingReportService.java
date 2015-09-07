package org.anyframe.cargotracker.interfaces.handling.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.anyframe.cargotracker.application.ApplicationEvents;
import org.anyframe.cargotracker.domain.model.cargo.TrackingId;
import org.anyframe.cargotracker.domain.model.handling.HandlingEvent;
import org.anyframe.cargotracker.domain.model.location.UnLocode;
import org.anyframe.cargotracker.domain.model.voyage.VoyageNumber;
import org.anyframe.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HandlingReportService {

	public final String ISO_8601_FORMAT = "yyyy-MM-dd HH:mm";
	
	private final Logger logger = LoggerFactory.getLogger(HandlingReportService.class);
	
	@Autowired
	private ApplicationEvents applicationEvents;

	@RequestMapping(value="/handling/reports", method=RequestMethod.POST)
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void submitReport(@RequestBody HandlingReport handlingReport) {
		try {
			Date completionTime = new SimpleDateFormat(ISO_8601_FORMAT).parse(
                    handlingReport.getCompletionTime());
            VoyageNumber voyageNumber = null;
            
            if (handlingReport.getVoyageNumber() != null) {
            	String temp = handlingReport.getVoyageNumber();
            	if("".equals(temp)) {
            		voyageNumber = null;
            	} else {
            		voyageNumber = new VoyageNumber(handlingReport.getVoyageNumber());
            	}
            }
            
            HandlingEvent.Type type = HandlingEvent.Type.valueOf(
                    handlingReport.getEventType());
            UnLocode unLocode = new UnLocode(handlingReport.getUnLocode());
            
            TrackingId trackingId = new TrackingId(handlingReport.getTrackingId());

            Date registrationTime = new Date();
            HandlingEventRegistrationAttempt attempt =
                    new HandlingEventRegistrationAttempt(registrationTime,
                    completionTime, trackingId, voyageNumber, type, unLocode);

            applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);
            logger.debug("## Registered receivedHandlingEventRegistrationAttempt event ##");
        } catch (/*ParseException*/ Exception ex) {
            throw new RuntimeException("Error parsing completion time", ex);
        }
    }
	
}