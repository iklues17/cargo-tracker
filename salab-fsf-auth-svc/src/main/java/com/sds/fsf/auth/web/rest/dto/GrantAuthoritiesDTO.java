package com.sds.fsf.auth.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class GrantAuthoritiesDTO {

    @Pattern(regexp = "^[a-z0-9\\.@]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    private List<String> roles;

    public GrantAuthoritiesDTO() {
    }

    public GrantAuthoritiesDTO(String login, String password, String firstName, String lastName, String email, String langKey,
                   List<String> roles) {
        this.login = login;
        this.roles = roles;
    }

    public String getLogin() {
        return login;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
        "login='" + login + '\'' +
        ", roles=" + roles +
        '}';
    }
}
