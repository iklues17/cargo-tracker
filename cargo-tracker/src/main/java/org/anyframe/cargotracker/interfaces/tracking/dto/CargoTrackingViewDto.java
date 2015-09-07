package org.anyframe.cargotracker.interfaces.tracking.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SDS
 * 
 * Track 정보를 조회한 결과를 담을 data transfer object
 */
public class CargoTrackingViewDto {
	/** 
	 *  cargo 의 포함내용은 
	 *  "trackingId": "ABC123",
        "misdirected": false,
        "statusText": "In port New York",
        "destination": "Helsinki",
        "eta": "03/12/2014 12:00 오전 KST",
        "nextExpectedActivity": "Next expected activity is to load cargo onto voyage 0200T in New York"
	 *  
	 */	
	
	/** 
	 *  "events" : [
	        {
	            "expected": true,
	            "description": "Received in Hong Kong, at 03/01/2014 12:00 오전 KST"
	        },
	        {
	            "expected": true,
	            "description": "Loaded onto voyage 0100S in Hong Kong, at 03/02/2014 12:00 오전 KST"
	        },
	        {
	            "expected": true,
	            "description": "Unloaded off voyage 0100S in New York, at 03/05/2014 12:00 오전 KST"
	        },
	    ]
	 * 
	 * */
	
	// list 의 events 객체	
    private final String trackingId;
    private final boolean misdirected;
    private final String statusText;
    private final String destination;
    private final String eta;
    private final String nextExpectedActivity;
    private final List<HandlingEventViewDto> handlingEventViewDto;
    
	public CargoTrackingViewDto(String trackingId, boolean misdirected,
			String statusText, String destination, String eta,
			String nextExpectedActivity) {
		this.trackingId = trackingId;
		this.misdirected = misdirected;
		this.statusText = statusText;
		this.destination = destination;
		this.eta = eta;
		this.nextExpectedActivity = nextExpectedActivity;
		this.handlingEventViewDto = new ArrayList<>();
	}

	public String getTrackingId() {
		return trackingId;
	}

	public boolean isMisdirected() {
		return misdirected;
	}


	public String getStatusText() {
		return statusText;
	}


	public String getDestination() {
		return destination;
	}


	public String getEta() {
		return eta;
	}


	public String getNextExpectedActivity() {
		return nextExpectedActivity;
	}

	public List<HandlingEventViewDto> getHandlingEventViewDto() {
		return handlingEventViewDto;
	}
	
	public void addHandlingEvent(boolean expected, String description){
		handlingEventViewDto.add(new HandlingEventViewDto(expected, description));
	}
}
