package org.anyframe.cargotracker.config;

import org.anyframe.cargotracker.infrastructure.events.spring.CargoEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {
	
	@Bean
	public CargoEventListener messageEventListener() {
		return new CargoEventListener();
	}
	
}
