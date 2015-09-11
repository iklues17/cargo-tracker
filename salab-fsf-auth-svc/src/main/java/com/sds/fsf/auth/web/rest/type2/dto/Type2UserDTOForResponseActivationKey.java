package com.sds.fsf.auth.web.rest.type2.dto;

import javax.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public class Type2UserDTOForResponseActivationKey extends Type2UserDTOForResponse{

    private boolean activated = true;
    
    @Size(max = 20)
    private String activationKey;    
    
    public Type2UserDTOForResponseActivationKey() {
    }

	public Type2UserDTOForResponseActivationKey(String login, String password,
			String firstName, String lastName, String email,
			String mobilePhoneNumber, String langKey, String authorityBase, List<String> roles, Map<String, String> infos,boolean activated, String activationKey) {
		super(login, password, firstName, lastName, email, mobilePhoneNumber, langKey, authorityBase,
				roles, infos);
		this.activated = activated;
		this.activationKey = activationKey;
	}

	public boolean isActivated() {
		return activated;
	}

	public String getActivationKey() {
		return activationKey;
	}

	@Override
	public String toString() {
		return "Type2UserDTOForResponseActivationKey [activated=" + activated
				+ ", activationKey=" + activationKey + ", getRoles()="
				+ getRoles() + ", getPassword()=" + getPassword()
				+ ", getLogin()=" + getLogin() + ", getFirstName()="
				+ getFirstName() + ", getLastName()=" + getLastName()
				+ ", getEmail()=" + getEmail() + ", getMobilePhoneNumber()="
				+ getMobilePhoneNumber() + ", getLangKey()=" + getLangKey()
				+ "]";
	}


}
