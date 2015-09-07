package org.anyframe.cargotracker.infrastructure.routing.service;

import org.anyframe.cargotracker.infrastructure.routing.TransitPaths;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "Pathfinder")
public interface PathFinderClient {
	@RequestMapping(method = RequestMethod.GET, value = "shortest-path", consumes = "application/json")
	TransitPaths findShortestPath(@RequestParam("origin") String origin,
			@RequestParam("destination") String destination,
			@RequestParam("deadline") String deadline);
}