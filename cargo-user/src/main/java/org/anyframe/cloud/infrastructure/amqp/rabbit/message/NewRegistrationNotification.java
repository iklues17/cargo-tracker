package org.anyframe.cloud.infrastructure.amqp.rabbit.message;

public class NewRegistrationNotification {
	private String id;
	private String emailAddress;
	
	public NewRegistrationNotification(){}
	
	public NewRegistrationNotification(String id, String emailAddress) {
		this.id = id;
		this.emailAddress = emailAddress;
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
}
