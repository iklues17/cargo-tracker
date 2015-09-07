package org.anyframe.cloud.application;

import org.anyframe.cloud.application.exception.ContentsNotExistException;
import org.anyframe.cloud.domain.RegisteredUser;
import org.anyframe.cloud.infrastructure.persistence.mongo.RegisteredUserMongoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonService {

	@Autowired
	protected RegisteredUserMongoDbRepository registeredUserRepository;

	public boolean isValidPassword(String emailAddress, String password){
		RegisteredUser target = registeredUserRepository.findByEmailAddress(emailAddress);
		if(target == null){
			throw new ContentsNotExistException("User is not existed", null);
		}else if(password.equals(target.getPassword())){
			return true;
		}else{
			return false;
		}
	}

}
