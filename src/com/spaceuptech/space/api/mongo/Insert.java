package com.spaceuptech.space.api.mongo;

import com.google.gson.Gson;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

/**
 * Class representing the Mongo Insert interface.
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
 * Post post = new Post("Post 1", "Some post");
 * db.insert("posts").one(post, responseListener);
 * </pre>
 */
public class Insert {
    private class Params {
        String op;
        Object doc;
    }

    private Config config;
    private String collection;
    private Params params;

    /**
     * Creates an instance of the Mongo Insert interface.
     * @param config config object
     * @param collection name of collection
     */
    public Insert(Config config, String collection) {
        this.config = config;
        this.collection = collection;
        this.params = new Params();
    }


    /**
     * Makes the query to insert a single document.
     * @param doc The document to be inserted.
     * @param listener Listener to listen to the response of insert query.
     * <pre>
     * db.insert("posts").one(post, responseListener);
     * </pre>
     */
    public void one(Object doc, Utils.ResponseListener listener) {
        this.params.op = "one";
        this.params.doc = doc;

        Utils.fetch(this.config.client,"post", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);
    }

    /**
     * Makes the query to insert multiple documents.
     * @param docs The documents to be inserted.
     * @param listener Listener to listen to the response of insert query.
     * <pre>
     * db.insert("posts").all(posts, responseListener);
     * </pre>
     */
    public void all(Object docs[], Utils.ResponseListener listener) {
        this.params.op = "all";
        this.params.doc = docs;

        Utils.fetch(this.config.client,"post", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);
    }
}
