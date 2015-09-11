package com.sds.fsf.auth.web.rest.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class UserIdRequestDTO {
	
    @Email
    @Size(min = 5, max = 100)
    private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}    
}
