package com.sds.fsf.auth.web.rest.type2.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Type2UserDTOForRegister {

    @Pattern(regexp = "^[a-z0-9\\.@]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = 5, max = 100)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;
    
    @Pattern(regexp = "^([\\+]{0,1}([0-9]+)|[(][\\+]{0,1}([0-9]+)[)])([\\-]{0,1}[0-9]+)*$")
    @Size(max = 100)
    private String mobilePhoneNumber;  

    @Size(min = 2, max = 5)
    private String langKey;

    @Pattern(regexp = "^(ROLE(_[A-Z][A-Z0-9]*)*)*$")
    @Size(min = 0, max = 50)
    private String authorityBase;
    
    public Type2UserDTOForRegister() {
    }

    public Type2UserDTOForRegister(String login, String password, String firstName, 
    		String lastName, String email, String mobilePhoneNumber, String langKey, String authorityBase) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.langKey = langKey;
        this.authorityBase = authorityBase;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
    
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public String getLangKey() {
        return langKey;
    }
    
    public String getAuthorityBase() {
		return authorityBase;
	}

	@Override
    public String toString() {
        return "UserDTO{" +
        "login='" + login + '\'' +
        ", password='" + password + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
        ", langKey='" + langKey + '\'' +
        ", authorityBase='" + authorityBase + '\'' +
        '}';
    }
}
