package org.anyframe.cloud.interfaces.rest.user;

import org.anyframe.cloud.application.exception.PasswordNotValid;
import org.anyframe.cloud.interfaces.facade.UserServiceFacade;
import org.anyframe.cloud.interfaces.facade.dto.IsRegistered;
import org.anyframe.cloud.interfaces.facade.dto.RegistrationRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserAccountRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

	@Autowired
	private UserServiceFacade registerServiceFacade;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@RequestMapping(value = {"/sign-up"}, method = {RequestMethod.POST})
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse registerUser(@RequestBody RegistrationRequest request){
		
		UserResponse response = registerServiceFacade.registerNewUser(request);
		return response;
		
	}
	
	@RequestMapping(value = {"/sign-up"}, method = {RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public IsRegistered isRegistered(@RequestParam(value="email", required=true) String emailAddress){
		
		IsRegistered response = registerServiceFacade.isRegistered(emailAddress);
		return response;
		
	}
	
	@RequestMapping(value = {"/withdrawal"}, method = {RequestMethod.POST})
	@ResponseStatus(HttpStatus.OK)
	public void withdrawalUser(@RequestBody UserAccountRequest request){
		
		registerServiceFacade.withdrawalUser(request);
		
	}

	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "duplicate email address")
	@ExceptionHandler(DuplicateKeyException.class)
	public void duplicateEmailAddress() {
	}

	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Password is wrong")
	@ExceptionHandler(PasswordNotValid.class)
	public void passwordNotValid() {
	}

}
