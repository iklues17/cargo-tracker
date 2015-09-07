package org.anyframe.cloud.application;

import org.anyframe.cloud.domain.RegisteredUser;
import org.anyframe.cloud.domain.UserId;
import org.anyframe.cloud.interfaces.facade.dto.RegistrationRequest;

/**
 * Cargo Tracker Management Portal's User Resgistration service
 * @author Hahn
 */
public interface RegistrationService {

	String registerNewUser(RegisteredUser registeredUser);

	void withdrawalUser(RegisteredUser registeredUser);

	RegisteredUser isRegistered(String emailAddress);
	
}
