package org.anyframe.cloud.application;

import java.util.List;

import org.anyframe.cloud.domain.RegisteredUser;


/**
 * Cargo Tracker Management Portal's User Management service
 * @author Hahn
 */
public interface ManagementService {

	RegisteredUser getUserByEmailAddress(String emailAddress);
	
	RegisteredUser getUserById(String id);

	List<RegisteredUser> getUserList();

	void modifyUser(RegisteredUser registeredUser);

	void changePassword(RegisteredUser registeredUser);
}
