package com.spaceuptech.space.api.utils;

import com.spaceuptech.space.api.mongo.Mongo;

public class MongoAuthResponse {
    public   String token;
    public MongoUser user;

    public MongoAuthResponse(String token, MongoUser user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public MongoUser getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "token: " + token + " sqlUser: " + user;
    }
}
