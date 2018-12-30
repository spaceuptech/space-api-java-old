package com.spaceuptech.space.api.sql;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.spaceuptech.space.api.API;
import com.spaceuptech.space.api.utils.*;

import java.util.HashMap;

import static com.spaceuptech.space.api.utils.Utils.createMap;

/**
 * Create an instance of the MongoDB Client Interface.
 */
public class SQL {
    private Config config;
    private String db;

    public SQL(String db, Config config) {
        this.db = db;
        this.config = config;
    }

    /**
     * @param collection Name of table to query.
     * @return SQL Get Object
     */
    public Get get(String collection) {
        return new Get(this.db, this.config, collection);
    }

    /**
     * @param collection Name of table to insert.
     * @return SQL Insert Object
     */
    public Insert insert(String collection) {
        return new Insert(this.db, this.config, collection);
    }

    /**
     * @param collection Name of table to update.
     * @return SQL Update Object
     */
    public Update update(String collection) {
        return new Update(this.db, this.config, collection);
    }

    /**
     * @param collection Name of table to delete.
     * @return SQL Delete Object
     */
    public Delete delete(String collection) {
        return new Delete(this.db, this.config, collection);
    }


    /**
     * Fetches profile for a given user.
     * @param id id of the user to fetch profile for.
     * @param listener listener to listen to the response.
     * <pre>
     * API api = new API("my-project", "http://localhost:8080");
     *
     * // For MySQL
     * SQL db = api.MySQL();
     *
     * // For Postgres
     * SQL db = api.Postgres();
     * Utils.SQLProfileListener sqlProfileListener = new Utils.SQLProfileListener() {
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
     * db.profile("some-user-id", sqlProfileListener);
     * </pre>
     */
    public void profile(String id, Utils.SQLProfileListener listener) {

        Utils.ResponseListener listener1 = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Response response) {
                Gson gson = new Gson();
                JsonElement userJsonElement = response.jsonObject.get("user");
                SQLUser sqlUser = gson.fromJson(userJsonElement, SQLUser.class);
                listener.onResponse(statusCode, sqlUser);
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        };

        Utils.fetch(this.config.client, "get", this.config.token,
                this.config.url + "v1/auth/" + this.db + "/profile/" + id, "", listener1);
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
     * SQL db = api.MySQL();
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

        Utils.fetch(this.config.client, "put", this.config.token,
                this.config.url + "v1/auth/" + this.db + "/profile/" + id,
                new Gson().toJson(createMap("record", map)), listener);
    }

    /**
     * Fetches profiles for all the users.
     * @param listener listener to listen to the response.
     * <pre>
     * API api = new API("my-project", "http://localhost:8080");
     * SQL db = api.MySQL();
     *
     * Utils.SQLProfilesListener sqlProfilesListener = new Utils.SQLProfilesListener() {
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
     * db.profiles(sqlProfilesListener);
     * </pre>
     */
    public void profiles(Utils.SQLProfilesListener listener) {

        Utils.ResponseListener listener1 = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Response response) {
                Gson gson = new Gson();
                JsonElement userJsonElement = response.jsonObject.get("users");
                SQLUser[] sqlUsers = gson.fromJson(userJsonElement, SQLUser[].class);
                listener.onResponse(statusCode, sqlUsers);
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        };

        Utils.fetch(this.config.client, "get", this.config.token,
                this.config.url + "v1/auth/" + this.db + "/profiles", "", listener1);
    }

    /**
     * Authenticate a user with provided email and password of the user.
     * @param email email of the user.
     * @param pass password of the user.
     * @param listener listener to listen to the response.
     * <pre>
     * Utils.SQLAuthListener sqlAuthListener = new Utils.SQLAuthListener() {
     *     {@code @Override}
     *     public void onResponse(int statusCode, SQLAuthResponse res) {
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
     * db.signIn("user1@gmail.com", "123", sqlAuthListener);
     * </pre>
     */
    public void signIn(String email, String pass, Utils.SQLAuthListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("pass", pass);
        map.put("db", this.db);

        Utils.ResponseListener listener1 = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Response response) {
                Gson gson = new Gson();
                SQLAuthResponse sqlAuthResponse = gson.fromJson(response.jsonObject, SQLAuthResponse.class);
                listener.onResponse(statusCode, sqlAuthResponse);
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
     * Utils.SQLAuthListener sqlAuthListener = new Utils.SQLAuthListener() {
     *     {@code @Override}
     *     public void onResponse(int statusCode, SQLAuthResponse res) {
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
     * db.signUp("user1@gmail.com", "User 1", "user", "123", sqlAuthListener);
     * </pre>
     */
    public void signUp(String email, String name, String pass, String role, Utils.SQLAuthListener listener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("name", name);
        map.put("pass", pass);
        map.put("role", role);
        map.put("db", this.db);

        Utils.ResponseListener listener1 = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Response response) {
                Gson gson = new Gson();
                SQLAuthResponse sqlAuthResponse = gson.fromJson(response.jsonObject, SQLAuthResponse.class);
                listener.onResponse(statusCode, sqlAuthResponse);
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        };

        Utils.fetch(this.config.client, "post", this.config.token, this.config.url + "v1/auth/email/signup",
                new Gson().toJson(map), listener1);
    }


    public static String sqlURL(String url, String db, String projectId, String table, String params) {
        String temp = url + "v1/sql/" + db + "/" + projectId + "/" + table;
        if (params.length() > 0) {
            temp += "?" + params;
        }
        return temp;
    }
}
