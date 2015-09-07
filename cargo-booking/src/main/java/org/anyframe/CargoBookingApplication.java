package org.anyframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CargoBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CargoBookingApplication.class, args);
    }
}
