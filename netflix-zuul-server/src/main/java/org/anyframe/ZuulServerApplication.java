package org.anyframe;

import org.anyframe.config.SimpleCORSFilter;
import org.anyframe.filter.auth.CheckAccessTokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ZuulServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }
	@Bean
	public SimpleCORSFilter SimpleCORSFilter() {
		SimpleCORSFilter filter = new SimpleCORSFilter();
	    return filter;
	}
	@Bean
	public CheckAccessTokenFilter CheckAccessTokenFilter() {
		return new CheckAccessTokenFilter();
	}
}
