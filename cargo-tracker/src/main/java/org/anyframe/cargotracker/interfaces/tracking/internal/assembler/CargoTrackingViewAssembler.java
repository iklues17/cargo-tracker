package org.anyframe.cargotracker.interfaces.tracking.internal.assembler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.anyframe.cargotracker.domain.model.cargo.Cargo;
import org.anyframe.cargotracker.domain.model.cargo.HandlingActivity;
import org.anyframe.cargotracker.domain.model.handling.HandlingEvent;
import org.anyframe.cargotracker.domain.model.location.Location;
import org.anyframe.cargotracker.interfaces.tracking.dto.CargoTrackingViewDto;

public class CargoTrackingViewAssembler {
	
	private Cargo cargo;
	private List<HandlingEvent> handlingEventList;
	private static final SimpleDateFormat DATE_FORMAT
    = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");	
	
	public CargoTrackingViewAssembler(Cargo cargo, List<HandlingEvent> handlingEventList){
		this.cargo = cargo;
		this.handlingEventList = handlingEventList;
	}
	
	
    public CargoTrackingViewDto toDto() {
    	CargoTrackingViewDto dto = new CargoTrackingViewDto(
    			this.cargo.getTrackingId().getIdString(),
    			this.cargo.getDelivery().isMisdirected(),
    			getStatusText(),
    			getDisplayText(this.cargo.getRouteSpecification().getDestination()),
    			getEta(),
    			getNextExpectedActivity());
    	
    	for (HandlingEvent handlingEvent : this.handlingEventList){
    		dto.addHandlingEvent(isExpected(handlingEvent), getDescription(handlingEvent));
    	}
    	
        return dto;
    }
    
	
    /**
     * @return A translated string describing the cargo status.
     */
    public String getStatusText() {

        switch (this.cargo.getDelivery().getTransportStatus()) {
            case IN_PORT:
                return "In port " + getDisplayText(this.cargo.getDelivery().getLastKnownLocation());
            case ONBOARD_CARRIER:
                return "Onboard voyage "
                        + this.cargo.getDelivery().getCurrentVoyage().getVoyageNumber().getIdString();
            case CLAIMED:
                return "Claimed";
            case NOT_RECEIVED:
                return "Not received";
            case UNKNOWN:
                return "Unknown";
            default:
                return "[Unknown status]"; // Should never happen.
        }
    }
    
    /**
     * @return A formatted string for displaying the location.
     */
    private String getDisplayText(Location location) {
        return location.getName();
    }
    
    public String getEta() {
        Date eta = this.cargo.getDelivery().getEstimatedTimeOfArrival();

        if (eta == null) {
            return "?";
        } else {
            return DATE_FORMAT.format(eta);
        }
    }
    
    public String getNextExpectedActivity() {
        HandlingActivity activity = this.cargo.getDelivery().getNextExpectedActivity();

        if ((activity == null) || (activity.isEmpty())) {
            return "";
        }

        String text = "Next expected activity is to ";
        HandlingEvent.Type type = activity.getType();

        if (type.sameValueAs(HandlingEvent.Type.LOAD)) {
            return text + type.name().toLowerCase() + " cargo onto voyage "
                    + activity.getVoyage().getVoyageNumber() + " in "
                    + activity.getLocation().getName();
        } else if (type.sameValueAs(HandlingEvent.Type.UNLOAD)) {
            return text + type.name().toLowerCase() + " cargo off of "
                    + activity.getVoyage().getVoyageNumber() + " in "
                    + activity.getLocation().getName();
        } else {
            return text + type.name().toLowerCase() + " cargo in "
                    + activity.getLocation().getName();
        }
    }
    
    public boolean isExpected(HandlingEvent handlingEvent) {
        return cargo.getItinerary().isExpected(handlingEvent);
    }

    public String getDescription(HandlingEvent handlingEvent) {
        switch (handlingEvent.getType()) {
            case LOAD:
                return "Loaded onto voyage "
                        + handlingEvent.getVoyage().getVoyageNumber().getIdString()
                        + " in " + handlingEvent.getLocation().getName() + ", at "
                        + DATE_FORMAT.format(handlingEvent.getCompletionTime()) + ".";
            case UNLOAD:
                return "Unloaded off voyage "
                        + handlingEvent.getVoyage().getVoyageNumber().getIdString()
                        + " in " + handlingEvent.getLocation().getName() + ", at "
                        + DATE_FORMAT.format(handlingEvent.getCompletionTime()) + ".";
            case RECEIVE:
                return "Received in " + handlingEvent.getLocation().getName()
                        + ", at " + DATE_FORMAT.format(handlingEvent.getCompletionTime()) + ".";
            case CLAIM:
                return "Claimed in " + handlingEvent.getLocation().getName()
                        + ", at " + DATE_FORMAT.format(handlingEvent.getCompletionTime()) + ".";
            case CUSTOMS:
                return "Cleared customs in " + handlingEvent.getLocation().getName()
                        + ", at " + DATE_FORMAT.format(handlingEvent.getCompletionTime()) + ".";
            default:
                return "[Unknown]";
        }
    }
}
