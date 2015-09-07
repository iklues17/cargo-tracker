package org.anyframe.cloud.application.internal;

import java.util.UUID;

import org.anyframe.cloud.application.CommonService;
import org.anyframe.cloud.application.RegistrationService;
import org.anyframe.cloud.application.exception.PasswordNotValid;
import org.anyframe.cloud.domain.RegisteredUser;
import org.anyframe.cloud.infrastructure.persistence.mongo.CompanyMongoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRegistrationService extends CommonService implements RegistrationService {

	@Autowired
	protected CompanyMongoDbRepository companyRepository;

	@Override
	public String registerNewUser(RegisteredUser registeredUser) {

		String random = UUID.randomUUID().toString().toUpperCase();
		
//		registeredUser.setUserId(new UserId(random.substring(0, random.indexOf("-"))));
		
		companyRepository.save(registeredUser.getCompany());
		
		registeredUserRepository.save(registeredUser);

		return registeredUser.getId();
	}

	public boolean isValidPassword(String emailAddress, String password){
		RegisteredUser target = registeredUserRepository.findByEmailAddress(emailAddress);
		if(target.getPassword().equals(password)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void withdrawalUser(RegisteredUser registeredUser) {
		
		if(isValidPassword(registeredUser.getEmailAddress(), registeredUser.getPassword())){
			RegisteredUser target = registeredUserRepository.findByEmailAddress(registeredUser.getEmailAddress());
			registeredUserRepository.delete(target.getId());
		}else{
			throw new PasswordNotValid("Password is wrong", null);
		}
		
	}

	@Override
	public RegisteredUser isRegistered(String emailAddress) {
		
		RegisteredUser target = registeredUserRepository.findByEmailAddress(emailAddress);
		
		return target;
	}

}
