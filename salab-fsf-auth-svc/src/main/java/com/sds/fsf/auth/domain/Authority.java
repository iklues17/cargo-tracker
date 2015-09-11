package com.sds.fsf.auth.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "FSF_AUTHORITY")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class Authority implements Serializable {

	private static final long serialVersionUID = 6213718926785419647L;
	
	@NotNull
    @Pattern(regexp = "^ROLE(_[A-Z][A-Z0-9]*)*$")
    @Size(min = 0, max = 50)
    @Id
    @Column(length = 50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "FSF_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> users = new HashSet<User>();
       
    public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority = (Authority) o;

        if (name != null ? !name.equals(authority.name) : authority.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + name + '\'' +
                "}";
    }
}
