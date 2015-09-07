package org.anyframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CargoTrackerPathfinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CargoTrackerPathfinderApplication.class, args);
    }
}
