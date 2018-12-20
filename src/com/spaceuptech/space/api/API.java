package com.spaceuptech.space.api;

import com.spaceuptech.space.api.mongo.Mongo;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;
import org.asynchttpclient.AsyncHttpClient;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class API {
    private Config config;

    public API(String projectId, String url) {
        if (url.endsWith("/")) url += "/";
        this.config = new Config(projectId, url, asyncHttpClient());
    }

    public void setToken(String token) {
        this.config.token = token;
    }

    public void setProjectId(String projectId) {
        this.config.projectId = projectId;
    }

    public Mongo Mongo() {
        return new Mongo(config);
    }
}
