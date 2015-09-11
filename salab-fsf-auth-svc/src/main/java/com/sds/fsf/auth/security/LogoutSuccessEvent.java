package com.sds.fsf.auth.security;

import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.core.Authentication;

public class LogoutSuccessEvent extends AbstractAuthenticationEvent {

	private static final long serialVersionUID = -739501160510475482L;

	public LogoutSuccessEvent(Authentication authentication) {
		super(authentication);
		// TODO Auto-generated constructor stub
	}

}
