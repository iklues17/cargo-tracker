package org.anyframe.cloud.interfaces.rest.user;

import org.anyframe.cloud.application.exception.ContentsNotExistException;
import org.anyframe.cloud.application.exception.PasswordNotValid;
import org.anyframe.cloud.interfaces.facade.UserServiceFacade;
import org.anyframe.cloud.interfaces.facade.dto.UserAccountRequest;
import org.anyframe.cloud.interfaces.facade.dto.UserResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@Autowired
	private UserServiceFacade loginServiceFacade;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@RequestMapping(value = {"/log-in"}, method = {RequestMethod.PUT})
	@ResponseStatus(HttpStatus.OK)
	public UserResponse login(@RequestBody UserAccountRequest request){
		
		UserResponse response = loginServiceFacade.login(request);
		return response;
		
	}
	
	@RequestMapping(value = {"/log-out"}, method = {RequestMethod.PUT})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout(@RequestBody UserAccountRequest request){
		
		loginServiceFacade.logout(request);
		
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "You are not member")
	@ExceptionHandler(ContentsNotExistException.class)
	public void contentsNotExistException() {
	}
	
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Password is wrong")
	@ExceptionHandler(PasswordNotValid.class)
	public void passwordNotValid() {
	}

}
