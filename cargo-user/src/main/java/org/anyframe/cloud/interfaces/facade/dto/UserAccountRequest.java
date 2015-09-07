package org.anyframe.cloud.interfaces.facade.dto;


public class UserAccountRequest {
	
	private String emailAddress;
	
	private String password;
	
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

}
