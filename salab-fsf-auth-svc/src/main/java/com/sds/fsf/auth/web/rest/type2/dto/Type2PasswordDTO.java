package com.sds.fsf.auth.web.rest.type2.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Type2PasswordDTO {

    @NotNull
    @Size(min = 5, max = 100)
    private String password;

    @NotNull
    @Size(min = 5, max = 100)
    private String passwordNew;    

    public Type2PasswordDTO() {
    }

    public Type2PasswordDTO(String password, String passwordNew) {
        this.password = password;
        this.passwordNew = passwordNew;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "PasswordDTO{" +
        ", password='" + password + '\'' +
        ", passwordNew='" + passwordNew + '\'' +
        '}';
    }

	public String getPasswordNew() {
		return passwordNew;
	}

	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}

}
