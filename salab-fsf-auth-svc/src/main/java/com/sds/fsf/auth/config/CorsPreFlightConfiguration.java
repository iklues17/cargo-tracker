package com.sds.fsf.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(-1)
@Configuration
@EnableWebSecurity
public class CorsPreFlightConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
		
        http
            .csrf().disable()
            .requestMatchers()
            	.antMatchers(HttpMethod.OPTIONS, "/**")
            	.and()
            .authorizeRequests()
            	.anyRequest().permitAll()
            	.and()
            	;
    }	
	
}
