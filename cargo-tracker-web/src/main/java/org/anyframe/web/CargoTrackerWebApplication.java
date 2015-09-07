package org.anyframe.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class CargoTrackerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CargoTrackerWebApplication.class, args);
    }
}
