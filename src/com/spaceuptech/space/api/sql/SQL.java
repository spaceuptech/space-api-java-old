package com.spaceuptech.space.api.sql;

import com.google.gson.Gson;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

import java.util.HashMap;

import static com.spaceuptech.space.api.utils.Utils.createMap;

public class SQL {
    private Config config;
    private String db;

    public SQL(String db, Config config) {
        this.db = db;
        this.config = config;
    }

    public Get get(String collection) {
        return new Get(this.db, this.config, collection);
    }

    public Insert insert(String collection) {
        return new Insert(this.db, this.config, collection);
    }

    public Update update(String collection) {
        return new Update(this.db, this.config, collection);
    }

    public Delete delete(String collection) {
        return new Delete(this.db, this.config, collection);
    }

    public void profile(String id, Utils.ResponseListener listener) {
        Utils.fetch(this.config.client, "get", this.config.token,
                this.config.url + "v1/auth/" + this.db + "/profile/" + id, "", listener);
    }

    public void editProfile(String id, String email, String name, String pass, Utils.ResponseListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("pass", pass);

        Utils.fetch(this.config.client, "put", this.config.token,
                this.config.url + "v1/auth/" + this.db + "/profile/" + id,
                new Gson().toJson(createMap("record", map)), listener);
    }

    public void profiles(Utils.ResponseListener listener) {
        Utils.fetch(this.config.client, "get", this.config.token,
                this.config.url + "v1/auth/" + this.db + "/profiles", "", listener);
    }

    public void signIn(String email, String pass, Utils.ResponseListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("pass", pass);
        map.put("db", this.db);

        Utils.fetch(this.config.client, "post", this.config.token, this.config.url + "v1/auth/email/signin",
                new Gson().toJson(map), listener);
    }

    public void signUp(String email, String name, String pass, String role, Utils.ResponseListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("pass", pass);
        map.put("role", role);
        map.put("db", this.db);

        Utils.fetch(this.config.client, "post", this.config.token, this.config.url + "v1/auth/email/signup",
                new Gson().toJson(map), listener);
    }


    public static String sqlURL(String url, String db, String projectId, String table, String params) {
        String temp = url + "v1/sql/" + db + "/" + projectId + "/" + table;
        if (params.length() > 0) {
            temp += "?" + params;
        }
        return temp;
    }
}
