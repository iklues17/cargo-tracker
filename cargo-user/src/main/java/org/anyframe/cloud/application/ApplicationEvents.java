package org.anyframe.cloud.application;

import org.anyframe.cloud.domain.RegisteredUser;

public interface ApplicationEvents {

	void userRegistered(RegisteredUser registeredUser);
}
