package com.spaceuptech.space.api.sql;

import com.google.gson.Gson;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

/**
 * Class representing the SQL Insert interface.
 * <pre>
 * API api = new API("test", "http://localhost:8080");
 *
 * // For MySQL
 * SQL db = api.MySQL();
 *
 * // For Postgres
 * SQL db = api.Postgres();
 *
 * Utils.ResponseListener responseListener = new Utils.ResponseListener() {
 *     {@code @Override}
 *     public void onResponse(int statusCode, Response response) {
 *         if (statusCode != 200) {
 *             System.out.println("Error: " + statusCode);
 *             return;
 *         }
 *      }
 *
 *     {@code @Override}
 *     public void onError(Exception e) {
 *         System.out.println("Exception: " + e);
 *     }
 * };
 *
 * Post post = new Post("Post 1", "Some post");
 * db.insert("posts").one(post, responseListener);
 * </pre>
 */
public class Insert {
    private class Params {
        String op;
        Object record;
    }

    private Config config;
    private String table, db;
    private Params params;

    /**
     * Creates an instance of the SQL Insert interface.
     * @param db name of database
     * @param config config object
     * @param table name of table
     */
    public Insert(String db, Config config, String table) {
        this.db = db;
        this.config = config;
        this.table = table;
        this.params = new Params();
    }

    /**
     * Makes the query to insert a single record.
     * @param record The record to be inserted.
     * @param listener Listener to listen to the response of insert query.
     * <pre>
     * db.insert("posts").one(post, responseListener);
     * </pre>
     */
    public void one(Object record, Utils.ResponseListener listener) {
        this.params.op = "one";
        this.params.record = record;

        Utils.fetch(this.config.client,"post", this.config.token,
                SQL.sqlURL(this.config.url, this.db, this.config.projectId, this.table, ""),
                new Gson().toJson(this.params), listener);
    }

    /**
     * Makes the query to insert multiple records.
     * @param records The records to be inserted.
     * @param listener Listener to listen to the response of insert query.
     * <pre>
     * db.insert("posts").all(posts, responseListener);
     * </pre>
     */
    public void all(Object records[], Utils.ResponseListener listener) {
        this.params.op = "all";
        this.params.record = records;

        Utils.fetch(this.config.client,"post", this.config.token,
                SQL.sqlURL(this.config.url, this.db, this.config.projectId, this.table, ""),
                new Gson().toJson(this.params), listener);
    }
}
