package org.anyframe;

import org.anyframe.cargotracker.infrastructure.messaging.message.ApplicationContextProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@EnableDiscoveryClient
@EnableFeignClients("org.anyframe.cargotracker.infrastructure.routing.service")
@Configuration
@EnableAutoConfiguration
@EnableHystrix
//@EnableRabbit
@ComponentScan(basePackages = "org.anyframe", useDefaultFilters = false, includeFilters = {
		@ComponentScan.Filter(Service.class),
		@ComponentScan.Filter(Repository.class),
		@ComponentScan.Filter(Component.class)})
public class CargoTrackerSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(CargoTrackerSpringApplication.class, args);
    }
    
    
    @Bean
	public ApplicationContextProvider applicationContextProvder() {
		ApplicationContextProvider applicationContextProvder = new ApplicationContextProvider();
		
		return applicationContextProvder;
	}
}
