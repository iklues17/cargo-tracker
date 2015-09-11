package com.sds.fsf.auth.security;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sds.fsf.auth.domain.User;
import com.sds.fsf.auth.domain.UserEventHistory;
import com.sds.fsf.auth.repository.UserEventHistoryRepository;
import com.sds.fsf.auth.repository.UserRepository;

@Component
public class LogInOutEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

	private final Logger log = LoggerFactory.getLogger(LogInOutEventListener.class);
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private UserEventHistoryRepository userEventHistoryRepository;	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		
		if(event instanceof AuthenticationSuccessEvent) {
			if(event.getSource() instanceof UsernamePasswordAuthenticationToken) {
				log.debug("AuthenticationEvent(Success) - UsernamePasswordAuthenticationToken");
				log.debug(event.toString());	

				UsernamePasswordAuthenticationToken source = (UsernamePasswordAuthenticationToken) event.getSource();
				
				User user = userRepository.findOneByLogin(source.getName());
				int count = 0;
				if(user.getLoginFailureCount() != null) {
					count = user.getLoginFailureCount();
					if(count > 0) {
						user.setLoginFailureCount(0);
						userRepository.save(user);	
					}
				}
				
				UserEventHistory userEvent = new UserEventHistory();
				userEvent.setUsername(source.getName());
				userEvent.setEventType(event.getClass().getName());
				userEventHistoryRepository.save(userEvent);
				
			} else {
				log.debug("AuthenticationEvent(Success) - Not UsernamePasswordAuthenticationToken");
				log.debug(event.toString());
			}
		} else if(event instanceof AbstractAuthenticationFailureEvent) {
			if(event.getSource() instanceof UsernamePasswordAuthenticationToken) {
				log.debug("AuthenticationEvent(Failure) - UsernamePasswordAuthenticationToken");
				log.debug(event.toString());	
				
				UsernamePasswordAuthenticationToken source = (UsernamePasswordAuthenticationToken) event.getSource();
				
				User user = userRepository.findOneByLogin(source.getName());
				if(user != null) {
					int count = 0;
					if(user.getLoginFailureCount() != null) {
						count = user.getLoginFailureCount();
					}
					count++;
					user.setLoginFailureCount(count);
					userRepository.save(user);
				}
				
				UserEventHistory userEvent = new UserEventHistory();
				userEvent.setUsername(source.getName());
				userEvent.setEventType(event.getClass().getName());
				userEventHistoryRepository.save(userEvent);				
				
			} else {
				log.debug("AuthenticationEvent(Failure) - Not UsernamePasswordAuthenticationToken");
				log.debug(event.toString());
			}
		} else if(event instanceof LogoutSuccessEvent) {
			OAuth2Authentication source = (OAuth2Authentication) event.getSource();
			
			UserEventHistory userEvent = new UserEventHistory();
			userEvent.setUsername(source.getName());
			userEvent.setEventType(event.getClass().getName());
			userEventHistoryRepository.save(userEvent);
		}
		

		
	}
	
}
