package com.spaceuptech.space.api;

import com.google.gson.Gson;
import com.spaceuptech.space.api.mongo.Mongo;
import com.spaceuptech.space.api.sql.SQL;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;
import org.asynchttpclient.AsyncHttpClient;

import java.util.HashMap;

import static org.asynchttpclient.Dsl.asyncHttpClient;

/**
 * Class representing the client API.
 */
public class API {
    private Config config;

    /**
     * Construct a client api for a given project. Has to be initialized only once for a project.
     * @param projectId Project Id
     * @param url URL of Space Exec
     * <pre>
     * API api = new API("my-project", "http://localhost:8080");
     * </pre>
     */
    public API(String projectId, String url) {
        if (!url.endsWith("/")) url += "/";
        this.config = new Config(projectId, url, asyncHttpClient());
    }

    /**
     * @param token The signed JWT token received from the server on successful authentication.
     */
    public void setToken(String token) {
        this.config.token = token;
    }

    /**
     * @param projectId Project Id.
     */
    public void setProjectId(String projectId) {
        this.config.projectId = projectId;
    }


    /**
     * @return MongoDB client instance
     */
    public Mongo Mongo() {
        return new Mongo(this.config);
    }

    /**
     * @return SQL client instance
     */
    public SQL MySQL() {
        return new SQL("mysql", this.config);
    }

    /**
     * @return SQL client instance
     */
    public SQL Postgres() {
        return new SQL("postgres", this.config);
    }


    /**
     * @param engineName The name of engine with which the function is registered
     * @param funcName The name of function to be called
     * @param timeout Timeout in milliseconds
     * @param params The params for the function
     * @param listener Listener for the response
     * <pre>
     * Utils.ResponseListener responseListener = new Utils.ResponseListener() {
     *     {@code @Override}
     *     public void onResponse(int statusCode, Response response) {
     *         System.out.println("FaaS Response: " + response.getValue(Object.class));
     *     }
     *
     *     {@code @Override}
     *     public void onError(Exception e) {
     *         System.out.println("Error: " + e.toString());
     *     }
     * };
     * api.call("echo-engine", "echo", 5000, "Space Cloud is awesome!", responseListener);
     *</pre>
     */
    public void call(String engineName, String funcName, int timeout, Object params, Utils.ResponseListener listener) {
        HashMap map = new HashMap<>();
        map.put("timeout", timeout);
        map.put("params", params);
        Utils.fetch(this.config.client, "post", this.config.token,
                this.config.url + "v1/functions/" + engineName + "/" + funcName,
                new Gson().toJson(map), listener);
    }
}