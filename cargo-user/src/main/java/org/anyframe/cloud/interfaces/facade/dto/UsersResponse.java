package org.anyframe.cloud.interfaces.facade.dto;

import java.util.List;

public class UsersResponse {
	
	private List<UserResponse> users;

	public UsersResponse(){}
	
	public UsersResponse(List<UserResponse> users) {
		this.users = users;
	}

	public List<UserResponse> getUsers() {
		return users;
	}

	public void setUsers(List<UserResponse> users) {
		this.users = users;
	}

}
