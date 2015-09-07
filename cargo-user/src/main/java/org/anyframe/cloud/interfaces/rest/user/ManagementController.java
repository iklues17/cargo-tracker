package org.anyframe.cloud.interfaces.rest.user;

import org.anyframe.cloud.application.exception.ContentsNotExistException;
import org.anyframe.cloud.interfaces.facade.UserServiceFacade;
import org.anyframe.cloud.interfaces.facade.dto.RegistrationRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserAccountRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserResponse;
import org.anyframe.cloud.interfaces.facade.dto.UsersResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagementController {

	@Autowired
	private UserServiceFacade managementService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@RequestMapping(value = {"/users/email"}, method = {RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public UserResponse getUser(@RequestParam(value="email", required=true) String emailAddress){
		UserResponse response = managementService.getUserByEmail(emailAddress);
		return response;
	}
	
	@RequestMapping(value = {"/users"}, method = {RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public UsersResponse getUsers(@RequestParam(value="role", required=false) String role){
		UsersResponse response = null;
		if(role == null){
			response = managementService.getUsers();
		}else{
//			response = managementService.getUsersByRole(role);
		}
		return response;
	}
	
	@RequestMapping(value = {"/users"}, method = {RequestMethod.PUT})
	@ResponseStatus(HttpStatus.OK)
	public UserResponse modifyUsers(@RequestBody RegistrationRequest request){
		UserResponse response = managementService.modifyUser(request);
		return response;
	}
	
	@RequestMapping(value = {"/users/password"}, method = {RequestMethod.PUT})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(@RequestBody UserAccountRequest request){
		managementService.changePassword(request);
	}

//	@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Contents are not exist")
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Contents are not exist")
	@ExceptionHandler(ContentsNotExistException.class)
	public void contentsNotExistException() {
	}

}
