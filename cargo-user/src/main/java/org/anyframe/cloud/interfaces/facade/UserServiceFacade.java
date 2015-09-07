package org.anyframe.cloud.interfaces.facade;

import org.anyframe.cloud.interfaces.facade.dto.IsRegistered;
import org.anyframe.cloud.interfaces.facade.dto.RegistrationRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserAccountRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserResponse;
import org.anyframe.cloud.interfaces.facade.dto.UsersResponse;


public interface UserServiceFacade {

	UserResponse registerNewUser(RegistrationRequest request);

	void withdrawalUser(UserAccountRequest request);

	IsRegistered isRegistered(String emailAddress);

	UserResponse getUserByEmail(String emailAddress);

	UsersResponse getUsers();

	UsersResponse getUsersByRole(String role);

	UserResponse login(UserAccountRequest request);

	void logout(UserAccountRequest request);

	UserResponse modifyUser(RegistrationRequest request);

	void changePassword(UserAccountRequest request);

}
