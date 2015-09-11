package com.sds.fsf.auth.web.rest.dto;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public class UserDTOIncludeRoles {

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

    @JsonInclude(Include.NON_NULL)
    @Pattern(regexp = "^([\\+]{0,1}([0-9]+)|[(][\\+]{0,1}([0-9]+)[)])([\\-]{0,1}[0-9]+)*$")
    @Size(max = 100)
    private String mobilePhoneNumber;      
    
    @Size(min = 2, max = 5)
    private String langKey;

    @JsonInclude(Include.NON_NULL)
    private List<String> roles;
    
    @JsonInclude(Include.NON_NULL)
    private Map<String, ?> infos;    

    public UserDTOIncludeRoles() {
    }

    public UserDTOIncludeRoles(String login, String password, String firstName, String lastName, String email, String mobilePhoneNumber, String langKey,
                   List<String> roles, Map<String, ?> infos) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.langKey = langKey;
        this.roles = roles;
        this.infos = infos;
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

    public List<String> getRoles() {
        return roles;
    }

    public Map<String, ?> getInfos() {
		return infos;
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
        ", roles=" + roles +
        ", infos=" + infos +
        '}';
    }
}
