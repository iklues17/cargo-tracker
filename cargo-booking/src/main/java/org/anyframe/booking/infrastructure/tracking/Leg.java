package org.anyframe.booking.infrastructure.tracking;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DTO for a leg in an itinerary.
 */
public class Leg implements Serializable {

	private static final long serialVersionUID = -3816111936846309672L;

	private static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

    private String voyageNumber;
    private String fromUnLocode;
    private String fromName;
    private String toUnLocode;
    private String toName;
    private String loadTime;
    private String unloadTime;

    public Leg() {
		super();
	}
    
    public Leg(
            String voyageNumber,
            String fromUnLocode,
            String fromName,
            String toUnLocode,
            String toName,
            String loadTime,
            String unloadTime) {
        this.voyageNumber = voyageNumber;
        this.fromUnLocode = fromUnLocode;
        this.fromName = fromName;
        this.toUnLocode = toUnLocode;
        this.toName = toName;
        this.loadTime = loadTime;
        this.unloadTime = unloadTime;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public String getFrom() {
        return fromName + " (" + fromUnLocode + ")";
    }

    public String getFromUnLocode() {
        return fromUnLocode;
    }

    public String getTo() {
        return toUnLocode + " (" + toName + ")";
    }

    public String getToUnLocode() {
        return toUnLocode;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public String getUnloadTime() {
        return unloadTime;
    }

    @Override
    public String toString() {
        return "Leg{" + "voyageNumber=" + voyageNumber + ", from=" + fromUnLocode + ", to=" + toUnLocode + ", loadTime=" + loadTime + ", unloadTime=" + unloadTime + '}';
    }
}
