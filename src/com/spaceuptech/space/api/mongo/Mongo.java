package com.spaceuptech.space.api.mongo;

import com.google.gson.Gson;
import com.spaceuptech.space.api.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

import static com.spaceuptech.space.api.utils.Utils.createMap;

public class Mongo {
    private Config config;

    public Mongo(Config config) {
        this.config = config;
    }

    public Get get(String collection) {
        return new Get(this.config, collection);
    }

    public Insert insert(String collection) {
        return new Insert(this.config, collection);
    }

    public Update update(String collection) {
        return new Update(this.config, collection);
    }

    public Delete delete(String collection) {
        return new Delete(this.config, collection);
    }

    public void profile(String id, Utils.ResponseListener listener) {
        Utils.fetch(this.config.client, "get", this.config.token, this.config.url + "v1/auth/profile/" + id,
                "", listener);
    }

    public void editProfile(String id, String email, String name, String pass, Utils.ResponseListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("pass", pass);

        Utils.fetch(this.config.client, "put", this.config.token, this.config.url + "v1/auth/profile/" + id,
                new Gson().toJson(createMap("update", createMap("$set", map))), listener);
    }

    public void profiles(Utils.ResponseListener listener) {
        Utils.fetch(this.config.client, "get", this.config.token, this.config.url + "v1/auth/profiles",
                "", listener);
    }

    public void signIn(String email, String pass, Utils.ResponseListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("pass", pass);

        Utils.fetch(this.config.client, "post", this.config.token, this.config.url + "v1/auth/email/signin",
                new Gson().toJson(map), listener);
    }

    public void signUp(String email, String name, String pass, String role, Utils.ResponseListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("pass", pass);
        map.put("role", role);

        Utils.fetch(this.config.client, "post", this.config.token, this.config.url + "v1/auth/email/signup",
                new Gson().toJson(map), listener);
    }

    public static String mongoURL(String url, String projectId, String collection, String params) {
        String temp = url + "v1/mongo/" + projectId + "/" + collection + "?" + params;
        if (params.length() > 0) {
            temp += "?" + params;
        }
        return temp;
    }
    
    public static HashMap<String, Object> generateFind(Condition condition) {
        switch (condition.condType) {
            case AND: {
                And and = (And) condition;
                HashMap<String, Object> map = new HashMap<>();
                for (Condition cond : and.conds) {
                    HashMap<String, Object> m = generateFind(cond);
                    map.putAll(m);
                }
                return map;
            }
            case OR: {
                Or or = (Or) condition;
                ArrayList<HashMap<String, Object>> conds = new ArrayList<>();
                for (Condition cond : or.conds) {
                    conds.add(generateFind(cond));
                }

                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("$or", conds);
                return map2;
            }
            case COND: {
                Cond cond = (Cond) condition;
                String f1 = cond.f1;
                Object f2 = cond.f2;

                HashMap<String, Object> map = new HashMap<>();
                switch (cond.eval) {
                    case "==":
                        map.put(f1, f2);
                        break;
                    case ">":
                        map.put(f1, createMap("$gt", f2));
                        break;

                    case "<":
                        map.put(f1, createMap("$lt", f2));
                        break;
                    case ">=":
                        map.put(f1, createMap("$gte", f2));
                        break;
                    case "<=":
                        map.put(f1, createMap("$lte", f2));
                        break;
                    case "!=":
                        map.put(f1, createMap("$ne", f2));
                        break;
                    case "in":
                        map.put(f1, createMap("$in", f2));
                        break;
                    case "notIn":
                        map.put(f1, createMap("$nin", f2));
                        break;
                }
                return map;
            }
            default: {
                return null;
            }
        }
    }
}
