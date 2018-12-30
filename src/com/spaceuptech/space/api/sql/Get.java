package com.spaceuptech.space.api.sql;

import com.google.gson.Gson;
import com.spaceuptech.space.api.mongo.Mongo;
import com.spaceuptech.space.api.utils.And;
import com.spaceuptech.space.api.utils.Condition;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

import java.util.HashMap;

/**
 * Class representing the SQL Get interface.
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
 *         Post[] posts = response.getValue(Post[].class);
 *      }
 *
 *     {@code @Override}
 *     public void onError(Exception e) {
 *         System.out.println("Exception: " + e);
 *     }
 * };
 *
 * db.get("posts")
 *     .where(Or.create(new Cond("title", "==", "Post 1"), new Cond("cat", "==", "Technology")))
 *     .all(responseListener);
 * </pre>
 */
public class Get {
    private class Params {
        HashMap<String, Object> find;
        int offset, limit;
        String[] order;
        HashMap<String, Integer> select;
        String op;
    }

    private String table, db;
    private Config config;

    private Params params;

    /**
     * Create an instance of the SQL Get Interface.
     * @param db
     * @param config
     * @param table
     */
    public Get(String db, Config config, String table) {
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
    public Get where(Condition... conds) {
        if (conds.length == 1) this.params.find = Mongo.generateFind(conds[0]);
        else this.params.find = Mongo.generateFind(And.create(conds));
        return this;
    }

    /**
     * Sets the fields to be selected. 1 to include and -1 to exclude a field from result.
     * @param select The select object.
     * @return
     * <pre>
     * HashMap selectClause = new HashMap<>();
     * selectClause.put("title", 1);
     * selectClause.put("author", -1);
     * db.get('posts').where(whereClause).select(selectClause).all(listener)
     * </pre>
     */
    public Get select(HashMap<String, Integer> select) {
        this.params.select = select;
        return this;
    }
    /**
     * Sets the fields to order result by. Negative sign for descending order.
     * @param order The fields to order result by.
     * @return
     * <pre>
     * db.get('posts').where(whereClause).order("title", "-date").all(listener)
     * </pre>
     */
    public Get order(String... order) {
        this.params.order = order;
        return this;
    }


    /**
     * Sets the number of records to skip in the array.
     * @param offset The number of records to skip.
     * @return
     * <pre>
     * db.get('posts').where(whereClause).offset(10).all(listener)
     * </pre>
     */
    public Get offset(int offset) {
        this.params.offset = offset;
        return this;
    }

    /**
     * Sets the limit on number of records returned by the query.
     * @param limit The limit on number of records.
     * @return
     * <pre>
     * db.get('posts').where(whereClause).limit(10).all(listener)
     * </pre>
     */
    public Get limit(int limit) {
        this.params.limit = limit;
        return this;
    }

    /**
     * Makes the query to get a single record as an object. If no record are returned, the status code is 400.
     * @param listener Listener to listen to the response of get query.
     * <pre>
     * db.get('posts').where(whereClause).one(listener)
     * </pre>
     */
    public void one(Utils.ResponseListener listener) {
        this.params.op = "one";

        Utils.fetch(this.config.client,"get", this.config.token, this.getUrl(), "", listener);
    }

    /**
     * Makes the query to get a multiple records as an array. It is possible for an empty array to be returned.
     * @param listener Listener to listen to the response of get query.
     * <pre>
     * db.get('posts').where(whereClause).all(listener)
     * </pre>
     */
    public void all(Utils.ResponseListener listener) {
        this.params.op = "all";

        Utils.fetch(this.config.client,"get", this.config.token, this.getUrl(), "", listener);
    }


    private String getUrl() {
        Gson gson = new Gson();

        String params = "op=" + this.params.op;
        params += "&find=" + gson.toJson(this.params.find);
        if (this.params.select != null && this.params.select.keySet().size() > 0) params += "&select=" + gson.toJson(this.params.select);
        if (this.params.order != null && this.params.order.length > 0) params += "&order=" + gson.toJson(this.params.order);
        if (this.params.limit > 0) params += "&limit=" + this.params.limit;
        if (this.params.offset > 0) params += "&skip=" + this.params.offset;
        return SQL.sqlURL(this.config.url, this.db, this.config.projectId, this.table, params);
    }
}
