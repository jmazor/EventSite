package me.vudb.backend.security;

import java.io.Serializable;
import java.util.List;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final List<String> roles;

    public JwtResponse(String jwttoken, List<String> roles) {
        this.jwttoken = jwttoken;
        this.roles = roles;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public List<String> getRoles() {
        return this.roles;
    }
}
