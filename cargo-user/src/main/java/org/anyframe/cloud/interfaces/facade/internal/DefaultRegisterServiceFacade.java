package org.anyframe.cloud.interfaces.facade.internal;

import java.util.List;

import org.anyframe.cloud.application.LoginService;
import org.anyframe.cloud.application.ManagementService;
import org.anyframe.cloud.application.RegistrationService;
import org.anyframe.cloud.domain.RegisteredUser;
import org.anyframe.cloud.interfaces.facade.UserServiceFacade;
import org.anyframe.cloud.interfaces.facade.dto.IsRegistered;
import org.anyframe.cloud.interfaces.facade.dto.RegistrationRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserAccountRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserResponse;
import org.anyframe.cloud.interfaces.facade.dto.UsersResponse;
import org.anyframe.cloud.interfaces.facade.internal.assembler.RegistrationDtoAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRegisterServiceFacade implements UserServiceFacade{

	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private ManagementService managementService;
	
	@Autowired
	private LoginService loginService;
	
//	@Autowired
//	private ApplicationEvents applicationEvents;

	@Override
	public UserResponse registerNewUser(RegistrationRequest request){
		
		RegisteredUser registeredUser = new RegistrationDtoAssembler().fromDto(null, request);
		
		registrationService.registerNewUser(registeredUser);
		
		// AMQP Message Publish
//		applicationEvents.userRegistered(registeredUser);
		
		return new RegistrationDtoAssembler().toDto(registeredUser); 
	}

	@Override
	public void withdrawalUser(UserAccountRequest request) {

		registrationService.withdrawalUser(new RegistrationDtoAssembler().fromDtoUserAccount(request));
	}

	@Override
	public IsRegistered isRegistered(String emailAddress) {
		
		RegisteredUser registeredUser = registrationService.isRegistered(emailAddress);
		
		IsRegistered isRegistered = new RegistrationDtoAssembler().toDtoIsRegistered(registeredUser);
		
		return isRegistered;
	}

	@Override
	public UserResponse getUserByEmail(String emailAddress) {
		
		RegisteredUser registeredUser = managementService.getUserByEmailAddress(emailAddress);
		
		return new RegistrationDtoAssembler().toDto(registeredUser);
	}

	@Override
	public UsersResponse getUsers() {
		
		List<RegisteredUser> registeredUserList = managementService.getUserList();
		
		return new RegistrationDtoAssembler().listToDto(registeredUserList);
	}

	@Override
	public UsersResponse getUsersByRole(String role) {
		return null;
		
	}

	@Override
	public UserResponse login(UserAccountRequest request) {

		RegisteredUser registeredUser = loginService.login(new RegistrationDtoAssembler().fromDtoUserAccount(request));
		return new RegistrationDtoAssembler().toDto(registeredUser);
	}

	@Override
	public void logout(UserAccountRequest request) {

		loginService.logout(new RegistrationDtoAssembler().fromDtoUserAccount(request));
		
	}

	@Override
	public UserResponse modifyUser(RegistrationRequest request) {
		
		RegisteredUser registeredUser = new RegistrationDtoAssembler().fromDto(null, request);
	
		managementService.modifyUser(registeredUser);
	
		
		return new RegistrationDtoAssembler().toDto(registeredUser);
	}

	@Override
	public void changePassword(UserAccountRequest request) {
		
		RegisteredUser registeredUser = new RegistrationDtoAssembler().fromDtoUserAccount(request);
	
		managementService.changePassword(registeredUser);
	}
}
