package com.sds.fsf.auth.web.rest.type2.dto;

import java.util.List;
import java.util.Map;

public class Type2UserDTOForResponse extends Type2UserDTOForRegister{

    private List<String> roles;
    
    private Map<String, String> infos;

    public Type2UserDTOForResponse() {
    }

    public Type2UserDTOForResponse(String login, String password,
			String firstName, String lastName, String email,
			String mobilePhoneNumber, String langKey, String authorityBase, 
			List<String> roles, Map<String, String> infos) {
		super(login, password, firstName, lastName, email, mobilePhoneNumber, langKey, authorityBase);
		this.roles = roles;
		this.infos = infos;
	}
    
	public List<String> getRoles() {
		return roles;
	}

	public Map<String, String> getInfos() {
		return infos;
	}

	@Override
	public String toString() {
		return "Type2UserDTOForResponse [roles=" + roles + ", infos=" + infos
				+ ", getPassword()=" + getPassword() + ", getLogin()="
				+ getLogin() + ", getFirstName()=" + getFirstName()
				+ ", getLastName()=" + getLastName() + ", getEmail()="
				+ getEmail() + ", getMobilePhoneNumber()="
				+ getMobilePhoneNumber() + ", getLangKey()=" + getLangKey()
				+ ", getAuthorityBase()=" + getAuthorityBase()
				+ "]";
	}
}
