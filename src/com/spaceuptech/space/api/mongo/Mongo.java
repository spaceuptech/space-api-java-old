package com.spaceuptech.space.api.mongo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.spaceuptech.space.api.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

import static com.spaceuptech.space.api.utils.Utils.createMap;

/**
 * Create an instance of the MongoDB Client Interface.
 */
public class Mongo {
    private Config config;

    public Mongo(Config config) {
        this.config = config;
    }

    /**
     * @param collection The collection to query documents.
     * @return MongoDB Get Object
     */
    public Get get(String collection) {
        return new Get(this.config, collection);
    }

    /**
     * @param collection The collection to insert documents.
     * @return MongoDB Insert Object
     */
    public Insert insert(String collection) {
        return new Insert(this.config, collection);
    }

    /**
     * @param collection The collection to update documents.
     * @return MongoDB Update Object
     */
    public Update update(String collection) {
        return new Update(this.config, collection);
    }

    /**
     * @param collection The collection to delete documents.
     * @return MongoDB Delete Object
     */
    public Delete delete(String collection) {
        return new Delete(this.config, collection);
    }

    /**
     * Fetches profile for a given user.
     * @param id id of the user to fetch profile for.
     * @param listener listener to listen to the response.
     * <pre>
     * API api = new API("my-project", "http://localhost:8080");
     * Mongo db = api.Mongo();
     *
     * Utils.MongoProfileListener mongoProfileListener = new Utils.MongoProfileListener() {
     *     {@code @Override}
     *     public void onResponse(int statusCode, SQLUser user) {
     *         if (statusCode == 200) {
     *             // Do whatever you want to do with user
     *         }
     *     }
     *
     *     {@code @Override}
     *     public void onError(Exception e) {
     *
     *     }
     * };
     *
     * db.profile("some-user-id", mongoProfileListener);
     * </pre>
     */
    public void profile(String id, Utils.MongoProfileListener listener) {

        Utils.ResponseListener listener1 = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Response response) {
                Gson gson = new Gson();
                JsonElement userJsonElement = response.jsonObject.get("user");
                MongoUser mongoUser = gson.fromJson(userJsonElement, MongoUser.class);
                listener.onResponse(statusCode, mongoUser);
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        };

        Utils.fetch(this.config.client, "get", this.config.token, this.config.url + "v1/auth/profile/" + id,
                "", listener1);
    }


    /**
     * Edits profile of a given user with the new details provided.
     * @param id id of the user whose profile needs to be edited.
     * @param email new email for the user.
     * @param name new name for the user.
     * @param pass new pass for the user.
     * @param listener listener to listen to the response.
     * <pre>
     * API api = new API("my-project", "http://localhost:8080");
     * Mongo db = api.Mongo();
     *
     * Utils.ResponseListener listener = new Utils.ResponseListener() {
     *     {@code @Override}
     *     public void onResponse(int statusCode, Response response) {
     *         if (statusCode == 200) {
     *
     *         }
     *     }
     *
     *     {@code @Override}
     *     public void onError(Exception e) {
     *
     *     }
     * };
     *
     * db.editProfile("some-user-id", "user1@gmail.com", "User 1", "123", sqlProfileListener);
     * </pre>
     */
    public void editProfile(String id, String email, String name, String pass, Utils.ResponseListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("pass", pass);

        Utils.fetch(this.config.client, "put", this.config.token, this.config.url + "v1/auth/profile/" + id,
                new Gson().toJson(createMap("update", createMap("$set", map))), listener);
    }

    /**
     * Fetches profiles for all the users.
     * @param listener listener to listen to the response.
     * <pre>
     * API api = new API("my-project", "http://localhost:8080");
     * Mongo db = api.Mongo();
     *
     * Utils.MongoProfilesListener mongoProfilesListener = new Utils.MongoProfilesListener() {
     *     {@code @Override}
     *     public void onResponse(int statusCode, SQLUser[] users) {
     *         if (statusCode == 200) {
     *             // Do whatever you want to do with users
     *         }
     *     }
     *
     *     {@code @Override}
     *     public void onError(Exception e) {
     *
     *     }
     * };
     *
     * db.profiles(mongoProfilesListener);
     * </pre>
     */
    public void profiles(Utils.MongoProfilesListener listener) {

        Utils.ResponseListener listener1 = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Response response) {
                Gson gson = new Gson();
                JsonElement userJsonElement = response.jsonObject.get("users");
                MongoUser[] mongoUsers = gson.fromJson(userJsonElement, MongoUser[].class);
                listener.onResponse(statusCode, mongoUsers);
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        };

        Utils.fetch(this.config.client, "get", this.config.token, this.config.url + "v1/auth/profiles",
                "", listener1);
    }

    /**
     * Authenticate a user with provided email and password of the user.
     * @param email email of the user.
     * @param pass password of the user.
     * @param listener listener to listen to the response.
     * <pre>
     * Utils.MongoAuthListener mongoAuthListener = new Utils.MongoAuthListener() {
     *     {@code @Override}
     *     public void onResponse(int statusCode, MongoAuthResponse res) {
     *         if (statusCode == 200) {
     *             // Do whatever you want to do with users
     *         }
     *     }
     *
     *     {@code @Override}
     *     public void onError(Exception e) {
     *
     *     }
     * };
     * db.signIn("user1@gmail.com", "123", mongoAuthListener);
     * </pre>
     */
    public void signIn(String email, String pass, Utils.MongoAuthListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("pass", pass);

        Utils.ResponseListener listener1 = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Response response) {
                Gson gson = new Gson();
                MongoAuthResponse mongoAuthResponse = gson.fromJson(response.jsonObject, MongoAuthResponse.class);
                listener.onResponse(statusCode, mongoAuthResponse);
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        };

        Utils.fetch(this.config.client, "post", this.config.token, this.config.url + "v1/auth/email/signin",
                new Gson().toJson(map), listener1);
    }


    /**
     * Create a new user with the provided details.
     * @param email email of the user.
     * @param name name of the user.
     * @param pass password of the user.
     * @param role role of the user.
     * @param listener listener to listen to the response.
     * <pre>
     * Utils.MongoAuthListener mongoAuthListener = new Utils.MongoAuthListener() {
     *     {@code @Override}
     *     public void onResponse(int statusCode, MongoAuthResponse res) {
     *         if (statusCode == 200) {
     *             // Do whatever you want to do with users
     *         }
     *     }
     *
     *     {@code @Override}
     *     public void onError(Exception e) {
     *
     *     }
     * };
     * db.signUp("user1@gmail.com", "User 1", "123", "user", mongoAuthListener);
     * </pre>
     */
    public void signUp(String email, String name, String pass, String role, Utils.MongoAuthListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("pass", pass);
        map.put("role", role);

        Utils.ResponseListener listener1 = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Response response) {
                Gson gson = new Gson();
                MongoAuthResponse mongoAuthResponse = gson.fromJson(response.jsonObject, MongoAuthResponse.class);
                listener.onResponse(statusCode, mongoAuthResponse);
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        };

        Utils.fetch(this.config.client, "post", this.config.token, this.config.url + "v1/auth/email/signup",
                new Gson().toJson(map), listener1);
    }

    public static String mongoURL(String url, String projectId, String collection, String params) {
        String temp = url + "v1/mongo/" + projectId + "/" + collection;
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
