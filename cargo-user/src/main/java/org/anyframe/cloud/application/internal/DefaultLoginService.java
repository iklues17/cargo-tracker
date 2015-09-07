package org.anyframe.cloud.application.internal;

import org.anyframe.cloud.application.CommonService;
import org.anyframe.cloud.application.LoginService;
import org.anyframe.cloud.application.exception.PasswordNotValid;
import org.anyframe.cloud.domain.RegisteredUser;
import org.springframework.stereotype.Service;

@Service
public class DefaultLoginService extends CommonService implements LoginService{

	@Override
	public RegisteredUser login(RegisteredUser userAccount) {
		RegisteredUser registeredUser = null;
		if(isValidPassword(userAccount.getEmailAddress(), userAccount.getPassword())){
			registeredUser = registeredUserRepository.findByEmailAddress(userAccount.getEmailAddress());
			// lastLogedin Time Update
			registeredUserRepository.save(registeredUser);
		}else{
			throw new PasswordNotValid("Password is wrong", null);
		}
		return registeredUser;
	}

	@Override
	public void logout(RegisteredUser userAccount) {
		//Token 만료
	}

}
