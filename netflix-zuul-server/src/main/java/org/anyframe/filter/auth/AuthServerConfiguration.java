package org.anyframe.filter.auth;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AuthServerConfiguration implements EnvironmentAware {
		
	private String baseUrl;
	private String getTokenUrl;
	private String checkTokenUrl;

	public String getBaseUrl() {
		return baseUrl;
	}
	public String getGetTokenUrl() {
		return getTokenUrl;
	}
	public String getCheckTokenUrl() {
		return checkTokenUrl;
	}

	@Override
	public void setEnvironment(Environment env) {
		this.baseUrl = env.getProperty("auth_server.base_url");
		this.checkTokenUrl = env.getProperty("auth_server.check_token_url");
		this.getTokenUrl = env.getProperty("auth_server.get_token_url");
	}
}
