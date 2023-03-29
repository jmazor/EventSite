package me.vudb.backend.security;

import java.io.Serializable;
import java.util.List;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final List<String> roles;

    private final String username;


    public JwtResponse(String jwttoken, List<String> roles, String username) {
        this.jwttoken = jwttoken;
        this.roles = roles;
        this.username = username;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public String getUsername() {
        return this.username;
    }
}
