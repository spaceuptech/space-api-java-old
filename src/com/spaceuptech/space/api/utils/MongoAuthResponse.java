package com.spaceuptech.space.api.utils;

import com.spaceuptech.space.api.mongo.Mongo;

/**
 * Response for authentication requests (signIm and signUp) for SQL Databases.
 */
public class MongoAuthResponse {
    public   String token;
    public MongoUser user;

    /**
     * @param token The signed JWT token received from the server on successful authentication.
     * @param user The user object representing user details.
     */
    public MongoAuthResponse(String token, MongoUser user) {
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
    public MongoUser getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "token: " + token + " sqlUser: " + user;
    }
}
