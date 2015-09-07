package org.anyframe.cargotracker.config;

import org.anyframe.cargotracker.infrastructure.messaging.message.CargoHandledConsumer;
import org.anyframe.cargotracker.infrastructure.messaging.message.HandlingEventRegistrationAttemptConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AmqpReceiverConfiguration {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
//	@Bean
//	public Queue misdirectedCargoQueue() {
//		return new Queue(env.getProperty("amqp.queue-name.misdirected"));
//	}
//	@Bean
//	public Binding misdirectedCargoBinding() {
//		return BindingBuilder.bind(misdirectedCargoQueue()).to(topicExchange()).with(env.getProperty("amqp.routingkey.misdirected"));
//	}
	
//	@Bean
//	public Queue deliveredCargoQueue() {
//		return new Queue(env.getProperty("amqp.queue-name.delivered"));
//	}
//	@Bean
//	public Binding deliveredCargoBinding() {
//		return BindingBuilder.bind(deliveredCargoQueue()).to(topicExchange()).with(env.getProperty("amqp.routingkey.delivered"));
//	}
	
	@Bean
	public Queue cargoHandledQueue() {
		return new Queue(env.getProperty("amqp.queue-name.cargoHandled"));
	}
	@Bean
	public Binding cargoHandledBinding() {
	    return BindingBuilder.bind(cargoHandledQueue()).to(topicExchange()).with(env.getProperty("amqp.routingkey.cargoHandled"));
	}
	
	@Bean
	public Queue handlingEventRegistrationAttemptQueue() {
		return new Queue(env.getProperty("amqp.queue-name.handlingEvent"));
	}
	@Bean
	public Binding handlingEventBinding() {
		return BindingBuilder.bind(handlingEventRegistrationAttemptQueue()).to(topicExchange()).with(env.getProperty("amqp.routingkey.handlingEvent"));
	}
	
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(env.getProperty("amqp.exchange.name"));
	}
		
	@Bean
	public SimpleMessageListenerContainer handlingEventQueueContainer() {		
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		MessageListenerAdapter adapter = new MessageListenerAdapter(new HandlingEventRegistrationAttemptConsumer());
		
		container.setConnectionFactory(connectionFactory);
		container.setMessageListener(adapter);
		container.setQueues(handlingEventRegistrationAttemptQueue());
		
		return container;
	}
	
	@Bean
	public SimpleMessageListenerContainer cargoHandledQueueContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		MessageListenerAdapter adapter = new MessageListenerAdapter(new CargoHandledConsumer());
	
		container.setConnectionFactory(connectionFactory);
		container.setQueues(cargoHandledQueue());
		container.setMessageListener(adapter);
		
		return container;
	}
	
//	@Bean
//	public SimpleMessageListenerContainer misdirectedCargoQueueContainer() {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//		MessageListenerAdapter adapter = new MessageListenerAdapter(new SimpleLoggingConsumer());
//	
//		container.setConnectionFactory(connectionFactory);
//		container.setQueues(misdirectedCargoQueue());
//		container.setMessageListener(adapter);
//		
//		return container;
//	}
	
//	@Bean
//	public SimpleMessageListenerContainer deliveredCargoQueueContainer() {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//		MessageListenerAdapter adapter = new MessageListenerAdapter(new SimpleLoggingConsumer());
//	
//		container.setConnectionFactory(connectionFactory);
//		container.setQueues(deliveredCargoQueue());
//		container.setMessageListener(adapter);
//		
//		return container;
//	}
	
}
