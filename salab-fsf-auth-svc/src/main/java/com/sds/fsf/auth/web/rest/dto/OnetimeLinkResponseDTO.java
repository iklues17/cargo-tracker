package com.sds.fsf.auth.web.rest.dto;

import java.io.Serializable;

public class OnetimeLinkResponseDTO implements Serializable {

	private static final long serialVersionUID = -3274515301762658694L;

	public OnetimeLinkResponseDTO(String onetimeLinkTochangePasswd) {
		this.onetimeLinkTochangePasswd = onetimeLinkTochangePasswd;
	}

	private String onetimeLinkTochangePasswd;

	public String getOnetimeLinkTochangePasswd() {
		return onetimeLinkTochangePasswd;
	}

	public void setOnetimeLinkTochangePasswd(String onetimeLinkTochangePasswd) {
		this.onetimeLinkTochangePasswd = onetimeLinkTochangePasswd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((onetimeLinkTochangePasswd == null) ? 0 : onetimeLinkTochangePasswd.hashCode());
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
		OnetimeLinkResponseDTO other = (OnetimeLinkResponseDTO) obj;
		if (onetimeLinkTochangePasswd == null) {
			if (other.onetimeLinkTochangePasswd != null)
				return false;
		} else if (!onetimeLinkTochangePasswd.equals(other.onetimeLinkTochangePasswd))
			return false;
		return true;
	}
}
