package com.sds.fsf.auth.web.rest.type2.dto;

import javax.validation.constraints.Size;

public class Type2UserDTOForUpdate extends Type2UserDTOForRegister{

    @Size(min = 5, max = 100)
    private String passwordNew;    

    public Type2UserDTOForUpdate() {
    }

    public Type2UserDTOForUpdate(String login, String password, 
    		String passwordNew, String firstName, String lastName, 
    		String email, String mobilePhoneNumber, String langKey, String authorityBase) {
    	super(login, password, firstName, lastName, email, mobilePhoneNumber, langKey, authorityBase);
        this.passwordNew = passwordNew;
    }
    
	public String getPasswordNew() {
		return passwordNew;
	}

	@Override
	public String toString() {
		return "Type2UserDTOForUpdate [passwordNew=" + passwordNew
				+ ", getPassword()=" + getPassword() + ", getLogin()="
				+ getLogin() + ", getFirstName()=" + getFirstName()
				+ ", getLastName()=" + getLastName() + ", getEmail()="
				+ getEmail() + ", getMobilePhoneNumber()="
				+ getMobilePhoneNumber() + ", getLangKey()=" + getLangKey()
				+ "]";
	}



}
