package org.anyframe.cloud.infrastructure.persistence;

import java.util.List;

import org.anyframe.cloud.domain.RegisteredUser;


public interface RegisteredUserRepository {
	
	RegisteredUser findByEmailAddress(String emailAddress);
	
	List<RegisteredUser> findByRole(String role);
}
