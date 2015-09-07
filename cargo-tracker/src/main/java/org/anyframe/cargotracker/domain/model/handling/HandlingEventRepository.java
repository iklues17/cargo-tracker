package org.anyframe.cargotracker.domain.model.handling;

import org.anyframe.cargotracker.domain.model.cargo.TrackingId;

public interface HandlingEventRepository {

    void store(HandlingEvent event);

    HandlingHistory lookupHandlingHistoryOfCargo(TrackingId trackingId);
}
