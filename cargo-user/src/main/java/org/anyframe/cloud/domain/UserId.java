package org.anyframe.cloud.domain;

import java.io.Serializable;

public class UserId implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;

    public UserId() {
    }

    public UserId(String id) {
        this.id = id;
    }

    public String getIdString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserId other = (UserId) o;

        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    boolean sameValueAs(UserId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
