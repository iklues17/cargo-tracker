package com.sds.fsf.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sds.fsf.cmm.domain.AbstractAuditingEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "FSF_USER")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class User extends AbstractAuditingEntity implements Serializable {
	
	private static final long serialVersionUID = 1055793339944882280L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-z0-9\\.@]*$")
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 5, max = 100)
    @Column(length = 100)
    private String password;
    
    @JsonProperty("password")
    @Transient
    private String transientPassword;

    public String getTransientPassword() {
		return transientPassword;
	}

	public void setTransientPassword(String transientPassword) {
		this.transientPassword = transientPassword;
	}

	@Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String email;
    
    @Pattern(regexp = "^([\\+]{0,1}([0-9]+)|[(][\\+]{0,1}([0-9]+)[)])([\\-]{0,1}[0-9]+)*$")
    @Column(name = "mobile_phone_number", length = 100, unique = true)
    private String mobilePhoneNumber;  
    
    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;
    
    @Column(nullable = false)
    private boolean activated = true;

    @JsonIgnore
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    private String activationKey;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "FSF_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Authority> authorities = new HashSet<Authority>();

    @Pattern(regexp = "^ROLE(_[A-Z][A-Z0-9]*)*$")
    @Size(min = 0, max = 50)
    @Column(name = "authority_base", length = 50)
    private String authorityBase = "ROLE";
     
    @Column(name = "login_failure_count")
    private Integer loginFailureCount = 0;
    
    
    public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "password_updated_date")
    private DateTime passwordUpdatedDate = DateTime.now();	
	
    public DateTime getPasswordUpdatedDate() {
		return passwordUpdatedDate;
	}

	public void setPasswordUpdatedDate(DateTime passwordUpdatedDate) {
		this.passwordUpdatedDate = passwordUpdatedDate;
	}

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "password_expired_date")
    private DateTime passwordExpiredDate;

	public DateTime getPasswordExpiredDate() {
		return passwordExpiredDate;
	}

	public void setPasswordExpiredDate(DateTime passwordExpiredDate) {
		this.passwordExpiredDate = passwordExpiredDate;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	
    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }
    
	public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getAuthorityBase() {
		return authorityBase;
	}

	public void setAuthorityBase(String authorityBase) {
		this.authorityBase = authorityBase;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!login.equals(user.login)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobilePhoneNumber='" + mobilePhoneNumber + '\'' +
                ", langKey='" + langKey + '\'' +
                ", activated='" + activated + '\'' +
                ", activationKey='" + activationKey + '\'' +
                "}";
    }
}
