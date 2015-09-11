package com.sds.fsf.auth.domain;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * A user.
 */
@Entity
@Table(name = "FSF_USER_EVENT_HISTORY")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class UserEventHistory implements Serializable {
	
	private static final long serialVersionUID = -2659916257852095668L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(name="username")
    private String username;

	@Column(name="event_type")
	private String eventType;
	
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name="event_time", nullable = false, length = 50, updatable = false)
    private DateTime eventTime = DateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public DateTime getEventTime() {
		return eventTime;
	}

	public void setEventTime(DateTime eventTime) {
		this.eventTime = eventTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventTime == null) ? 0 : eventTime.hashCode());
		result = prime * result
				+ ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		UserEventHistory other = (UserEventHistory) obj;
		if (eventTime == null) {
			if (other.eventTime != null)
				return false;
		} else if (!eventTime.equals(other.eventTime))
			return false;
		if (eventType == null) {
			if (other.eventType != null)
				return false;
		} else if (!eventType.equals(other.eventType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserEventHistory [id=" + id + ", username=" + username
				+ ", eventType=" + eventType + ", eventTime=" + eventTime + "]";
	}
    
    
}
