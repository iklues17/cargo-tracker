package org.anyframe.cargotracker.domain.service;

import java.util.List;
import org.anyframe.cargotracker.domain.model.cargo.Itinerary;
import org.anyframe.cargotracker.domain.model.cargo.RouteSpecification;

public interface RoutingService {

    /**
     * @param routeSpecification route specification
     * @return A list of itineraries that satisfy the specification. May be an
     * empty list if no route is found.
     */
    List<Itinerary> fetchRoutesForSpecification(RouteSpecification routeSpecification);
}
