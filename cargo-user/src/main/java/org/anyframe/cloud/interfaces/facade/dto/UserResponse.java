package org.anyframe.cloud.interfaces.facade.dto;

import org.anyframe.cloud.domain.Company;

public class UserResponse {
	
	private String id;
	
	private String emailAddress;
	
	private String firstName;
	
	private String lastName;
	
	private String role;
	
	private Company company;
	
	public UserResponse(){}
	
	public UserResponse(String id, String emailAddress,
			String firstName, String lastName, String role, Company company) {
		this.id = id;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.company = company;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

}
