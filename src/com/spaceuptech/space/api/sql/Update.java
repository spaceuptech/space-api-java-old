package com.spaceuptech.space.api.sql;

import com.google.gson.Gson;
import com.spaceuptech.space.api.mongo.Mongo;
import com.spaceuptech.space.api.utils.And;
import com.spaceuptech.space.api.utils.Condition;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

import java.util.HashMap;

/**
 * Class representing the SQL Update interface.
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
 * HashMap map = new HashMap<>();
 * map.put("content", "This is a good post");
 * db.update("posts")
 *     .where(new Cond("title", "==", "Post 1"))
 *     .set(map)
 *     .one(responseListener);
 * </pre>
 */
public class Update {
    private class Params {
        String op;
        HashMap<String, Object> find, record;
    }

    private Config config;
    private String table, db;
    private Params params;

    /**
     * Create an instance of the SQL Update Interface.
     * @param db
     * @param config
     * @param table
     */
    public Update(String db, Config config, String table) {
        this.db = db;
        this.config = config;
        this.table = table;
        this.params = new Params();
    }

    /**
     * Prepares the find query.
     * @param conds The logic for where clause.
     * @return
     */
    public Update where(Condition... conds) {
        if (conds.length == 1) this.params.find = Mongo.generateFind(conds[0]);
        else this.params.find = Mongo.generateFind(And.create(conds));
        return this;
    }

    /**
     * Sets the value of a field in a document.
     * @param obj The Object containing fields to set.
     * @return
     * <pre>
     * db.update("posts").where(whereClause).set(fields).all(listener);
     * </pre>
     */
    public Update set(HashMap<String, Object> obj) {
        this.params.record = obj;
        return this;
    }


    /**
     * Makes the query to update a single record which matches first.
     * @param listener Listener to listen to the response of update query.
     * <pre>
     * db.update("posts").where(whereClause).set(fields).one(listener);
     * </pre>
     */
    public void one(Utils.ResponseListener listener) {
        this.params.op = "one";
        Utils.fetch(this.config.client,"put", this.config.token,
                SQL.sqlURL(this.config.url, this.db, this.config.projectId, this.table, ""),
                new Gson().toJson(this.params), listener);

    }

    /**
     * Makes the query to update all records which matches.
     * @param listener Listener to listen to the response of update query.
     * <pre>
     * db.update("posts").where(whereClause).set(fields).all(listener);
     * </pre>
     */
    public void all(Utils.ResponseListener listener) {
        this.params.op = "all";
        Utils.fetch(this.config.client,"put", this.config.token,
                SQL.sqlURL(this.config.url, this.db, this.config.projectId, this.table, ""),
                new Gson().toJson(this.params), listener);

    }
}
