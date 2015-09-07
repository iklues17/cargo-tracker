package org.anyframe.cargotracker.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpSenderConfiguration {
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		
		rabbitTemplate.setConnectionFactory(connectionFactory);
		rabbitTemplate.setEncoding("UTF-8");
		
		return rabbitTemplate;
    }
	
}
