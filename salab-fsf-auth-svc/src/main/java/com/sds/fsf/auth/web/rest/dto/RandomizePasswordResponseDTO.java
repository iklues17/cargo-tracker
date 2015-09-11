package com.sds.fsf.auth.web.rest.dto;

import java.io.Serializable;

public class RandomizePasswordResponseDTO implements Serializable {

	private static final long serialVersionUID = -3274515301762658694L;

	public RandomizePasswordResponseDTO(String password) {
		this.password = password;
	}

	private String password;

	public String getPassword() {
		return password;
	}

	public void setOnetimeLinkTochangePasswd(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RandomizePasswordResponseDTO other = (RandomizePasswordResponseDTO) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
}
