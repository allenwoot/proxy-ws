package com.jab.proxy.web_service.beans;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class User {
    private String authToken;
    private String email;
    private String firstName;
    private String lastName;
    private String number;
    private String password;
    private UserType userType;

    public String getAuthToken() {
        return this.authToken;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getNumber() {
        return this.number;
    }

    public String getPassword() {
        return this.password;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUserType(final UserType userType) {
        this.userType = userType;
    }
}
