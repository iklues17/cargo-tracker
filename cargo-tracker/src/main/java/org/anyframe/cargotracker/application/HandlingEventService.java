package org.anyframe.cargotracker.application;

import java.util.Date;
import org.anyframe.cargotracker.domain.model.cargo.TrackingId;
import org.anyframe.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import org.anyframe.cargotracker.domain.model.handling.HandlingEvent;
import org.anyframe.cargotracker.domain.model.location.UnLocode;
import org.anyframe.cargotracker.domain.model.voyage.VoyageNumber;

public interface HandlingEventService {

    /**
     * Registers a handling event in the system, and notifies interested parties
     * that a cargo has been handled.
     */
    void registerHandlingEvent(Date completionTime,
            TrackingId trackingId,
            VoyageNumber voyageNumber,
            UnLocode unLocode,
            HandlingEvent.Type type) throws CannotCreateHandlingEventException;
}
