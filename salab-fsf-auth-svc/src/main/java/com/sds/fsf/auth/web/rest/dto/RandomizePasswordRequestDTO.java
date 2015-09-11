package com.sds.fsf.auth.web.rest.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class RandomizePasswordRequestDTO {
	
    @Pattern(regexp = "^[a-z0-9\\.@]*$")
    @Size(min = 1, max = 50)
    private String login;

    @Email
    @Size(min = 5, max = 100)
    private String email;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}    
}
