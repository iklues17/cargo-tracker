package org.anyframe.cloud.application;

import org.anyframe.cloud.domain.RegisteredUser;


/**
 * Cargo Tracker Management Portal's User Login service
 * @author Hahn
 */
public interface LoginService {

	RegisteredUser login(RegisteredUser userAccount);

	void logout(RegisteredUser userAccount);
	
}
