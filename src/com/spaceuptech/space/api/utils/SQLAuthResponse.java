package com.spaceuptech.space.api.utils;

/**
 * Response for authentication requests (signIm and signUp) for SQL Databases.
 */
public class SQLAuthResponse {
    public   String token;
    public SQLUser user;

    /**
     * @param token The signed JWT token received from the server on successful authentication.
     * @param user The user object representing user details.
     */
    public SQLAuthResponse(String token, SQLUser user) {
        this.token = token;
        this.user = user;
    }

    /**
     * @return token - Get JWT token signed from the server.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return user - Get user object.
     */
    public SQLUser getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "token: " + token + " sqlUser: " + user;
    }
}
