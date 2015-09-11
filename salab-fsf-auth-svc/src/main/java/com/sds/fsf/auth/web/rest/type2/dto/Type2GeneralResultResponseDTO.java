package com.sds.fsf.auth.web.rest.type2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Type2GeneralResultResponseDTO {

    private String result;

    @JsonInclude(Include.NON_NULL)
    private String reason;

	public Type2GeneralResultResponseDTO() {
	}    
    
	public Type2GeneralResultResponseDTO(String result, String reason) {
		this.result = result;
		this.reason = reason;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
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
		Type2GeneralResultResponseDTO other = (Type2GeneralResultResponseDTO) obj;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		return true;
	}    


}
