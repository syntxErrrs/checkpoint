package com.army.swf.checkpoint.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class AuthDTO {
    boolean authenticated;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserDTO user;

    public AuthDTO(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public AuthDTO(boolean authenticated, UserDTO user) {
        this.authenticated = authenticated;
        this.user = user;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public UserDTO getUser() {
        return user;
    }
}
