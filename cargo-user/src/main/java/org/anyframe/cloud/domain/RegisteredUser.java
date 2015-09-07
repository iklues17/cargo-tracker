package org.anyframe.cloud.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user")
public class RegisteredUser {
	
	@Id
	private String id;
	
	@Indexed(unique=true)
	private String emailAddress;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String role;
	
	@DBRef
	private Company company;
	
	@LastModifiedBy 
	private String lastModifiedBy;
	
	@LastModifiedDate
	private DateTime lastLogedin;
	
	public RegisteredUser(){}
	
	
	public RegisteredUser(String id, String emailAddress,
			String password, String firstName, String lastName, String role, Company company) {
		this.id = id;
		this.emailAddress = emailAddress;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.company = company;
	}

	public RegisteredUser(String emailAddress, String password) {
		this.emailAddress = emailAddress;
		this.password = password;
	}

	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}


	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}


	public DateTime getLastLogedin() {
		return lastLogedin;
	}

	public void setLastLogedin(DateTime lastLogedin) {
		this.lastLogedin = lastLogedin;
	}
	
}
