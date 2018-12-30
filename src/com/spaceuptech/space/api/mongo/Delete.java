package com.spaceuptech.space.api.mongo;

import com.google.gson.Gson;
import com.spaceuptech.space.api.utils.And;
import com.spaceuptech.space.api.utils.Condition;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

import java.util.HashMap;

/**
 * Class representing the Mongo Delete interface.
 * <pre>
 * API api = new API("test", "http://localhost:8080");
 * Mongo db = api.Mongo();
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
 * db.delete("posts").where(new Cond("title", "==", "Post 1")).all(responseListener);
 * </pre>
 */
public class Delete {
    private class Params {
        String op;
        HashMap<String, Object> find;

    }

    private Config config;
    private String collection;
    private Params params;

    /**
     * @param config config object
     * @param collection name of collection
     */
    public Delete(Config config, String collection) {
        this.config = config;
        this.collection = collection;
        this.params = new Params();
    }

    /**
     * Prepares the find query.
     * @param conds The logic for where clause.
     * @return
     */
    public Delete where(Condition... conds) {
        if (conds.length == 1) this.params.find = Mongo.generateFind(conds[0]);
        else this.params.find = Mongo.generateFind(And.create(conds));
        return this;
    }

    /**
     * Makes the query to delete the first doc which matches.
     * @param listener Listener to listen to the response of delete query.
     * <pre>
     * db.delete("posts").where(whereClause).one(listener)
     * </pre>
     */
    public void one(Utils.ResponseListener listener) {
        this.params.op = "one";
        Utils.fetch(this.config.client,"delete", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);

    }

    /**
     * Makes the query to delete all the docs which match.
     * @param listener Listener to listen to the response of delete query.
     * <pre>
     * db.delete("posts").where(whereClause).all(listener)
     * </pre>
     */
    public void all(Utils.ResponseListener listener) {
        this.params.op = "all";
        Utils.fetch(this.config.client,"delete", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);

    }

}
