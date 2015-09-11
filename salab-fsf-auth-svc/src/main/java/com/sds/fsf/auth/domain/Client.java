package com.sds.fsf.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "OAUTH_CLIENT_DETAILS")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class Client implements Serializable {
	
	private static final long serialVersionUID = 3257878296313519125L;

	@Id
    @Pattern(regexp = "^[A-Za-z0-9\\-\\.@]*$")
    @Column(name="client_id")
    private String clientId;
 
    @NotNull
    @Column(name="client_secret")
    private String clientSecret;    
    
    @Column(name="resource_ids")
    private String resourceIds;

    @Column(name="scope")
    private String scope;
    
    @NotNull
    @Column(name="authorized_grant_types")
    private String authorizedGrantTypes;
    
    @Column(name="web_server_redirect_uri")
    private String webServerRedirectUri;
    
    @NotNull
    @Column(name="authorities")
    private String authorities;
    
    @NotNull
    @Column(name="access_token_validity")
    private int accessTokenValidity = 1800;
    
    @NotNull
    @Column(name="refresh_token_validity")
    private int refreshTokenValidity = 18000;
    
    @Column(name="additional_information")
    private String additionalInformation;
    
    @Column(name="autoapprove")
    private String autoapprove;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "FSF_CLIENT_GRANT_DEFAULT_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "client_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Authority> clientGrantDefaultUserAuthorities = new HashSet<Authority>();
    
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAuthorizedGrantTypes() {
		return authorizedGrantTypes;
	}

	public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}

	public String getWebServerRedirectUri() {
		return webServerRedirectUri;
	}

	public void setWebServerRedirectUri(String webServerRedirectUri) {
		this.webServerRedirectUri = webServerRedirectUri;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public int getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(int accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}

	public int getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(int refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public String getAutoapprove() {
		return autoapprove;
	}

	public void setAutoapprove(String autoapprove) {
		this.autoapprove = autoapprove;
	}

    public Set<Authority> getClientGrantDefaultUserAuthorities() {
        return clientGrantDefaultUserAuthorities;
    }

    public void setClientGrantDefaultUserAuthorities(Set<Authority> clientGrantDefaultUserAuthorities) {
        this.clientGrantDefaultUserAuthorities = clientGrantDefaultUserAuthorities;
    }	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accessTokenValidity;
		result = prime
				* result
				+ ((additionalInformation == null) ? 0 : additionalInformation
						.hashCode());
		result = prime * result
				+ ((authorities == null) ? 0 : authorities.hashCode());
		result = prime
				* result
				+ ((authorizedGrantTypes == null) ? 0 : authorizedGrantTypes
						.hashCode());
		result = prime * result
				+ ((autoapprove == null) ? 0 : autoapprove.hashCode());
		result = prime * result
				+ ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result
				+ ((clientSecret == null) ? 0 : clientSecret.hashCode());
		result = prime * result + refreshTokenValidity;
		result = prime * result
				+ ((resourceIds == null) ? 0 : resourceIds.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime
				* result
				+ ((webServerRedirectUri == null) ? 0 : webServerRedirectUri
						.hashCode());
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
		Client other = (Client) obj;
		if (accessTokenValidity != other.accessTokenValidity)
			return false;
		if (additionalInformation == null) {
			if (other.additionalInformation != null)
				return false;
		} else if (!additionalInformation.equals(other.additionalInformation))
			return false;
		if (authorities == null) {
			if (other.authorities != null)
				return false;
		} else if (!authorities.equals(other.authorities))
			return false;
		if (authorizedGrantTypes == null) {
			if (other.authorizedGrantTypes != null)
				return false;
		} else if (!authorizedGrantTypes.equals(other.authorizedGrantTypes))
			return false;
		if (autoapprove == null) {
			if (other.autoapprove != null)
				return false;
		} else if (!autoapprove.equals(other.autoapprove))
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (clientSecret == null) {
			if (other.clientSecret != null)
				return false;
		} else if (!clientSecret.equals(other.clientSecret))
			return false;
		if (refreshTokenValidity != other.refreshTokenValidity)
			return false;
		if (resourceIds == null) {
			if (other.resourceIds != null)
				return false;
		} else if (!resourceIds.equals(other.resourceIds))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		if (webServerRedirectUri == null) {
			if (other.webServerRedirectUri != null)
				return false;
		} else if (!webServerRedirectUri.equals(other.webServerRedirectUri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OauthClientDetail [clientId=" + clientId + ", clientSecret="
				+ clientSecret + ", resourceIds=" + resourceIds + ", scope="
				+ scope + ", authorizedGrantTypes=" + authorizedGrantTypes
				+ ", webServerRedirectUri=" + webServerRedirectUri
				+ ", authorities=" + authorities + ", accessTokenValidity="
				+ accessTokenValidity + ", refreshTokenValidity="
				+ refreshTokenValidity + ", additionalInformation="
				+ additionalInformation + ", autoapprove=" + autoapprove + "]";
	}
    
    
}
