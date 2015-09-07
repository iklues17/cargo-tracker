package org.anyframe.cargotracker.infrastructure.messaging.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleLoggingConsumer {

    private final Logger logger = LoggerFactory.getLogger(SimpleLoggingConsumer.class);
    
    public void handleMessage(String message) {
    	logger.debug("Received AMQP message: {}", message);
    }
    
}
