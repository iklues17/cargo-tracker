package org.anyframe.cloud.interfaces.facade.internal.assembler;

import java.util.ArrayList;
import java.util.List;

import org.anyframe.cloud.domain.RegisteredUser;
import org.anyframe.cloud.interfaces.facade.dto.IsRegistered;
import org.anyframe.cloud.interfaces.facade.dto.RegistrationRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserAccountRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserResponse;
import org.anyframe.cloud.interfaces.facade.dto.UsersResponse;


// TODO Convert to a singleton and inject?
public class RegistrationDtoAssembler {
	
	public UsersResponse listToDto(List<RegisteredUser> registeredUserList) {
		List<UserResponse> usersResponse = new ArrayList<UserResponse>();
		
		for(RegisteredUser user : registeredUserList){
			usersResponse.add(this.toDto(user));
		}
		return new UsersResponse(usersResponse);
	}

    public UserResponse toDto(RegisteredUser registeredUser) {
    	return new UserResponse(
                registeredUser.getId(),
                registeredUser.getEmailAddress(),
                registeredUser.getFirstName(),
                registeredUser.getLastName(),
                registeredUser.getRole(),
    			registeredUser.getCompany());
    }

    public IsRegistered toDtoIsRegistered(RegisteredUser registeredUser) {
    	boolean isRegistered = false;
    	if(registeredUser != null && registeredUser.getId() != null){
    		isRegistered = true;
    	}
    	return new IsRegistered(isRegistered);
    }
    
    public RegisteredUser fromDto(String id, RegistrationRequest registrationRequest){
    	return new RegisteredUser(id,
    			registrationRequest.getEmailAddress(),
    			registrationRequest.getPassword(),
    			registrationRequest.getFirstName(),
    			registrationRequest.getLastName(),
    			registrationRequest.getRole(),
    			registrationRequest.getCompany());
    }

	public RegisteredUser fromDtoUserAccount(UserAccountRequest request) {
    	return new RegisteredUser(request.getEmailAddress(),request.getPassword());
	}
}
