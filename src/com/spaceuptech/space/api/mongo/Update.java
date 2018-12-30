package com.spaceuptech.space.api.mongo;

import com.google.gson.Gson;
import com.spaceuptech.space.api.utils.And;
import com.spaceuptech.space.api.utils.Condition;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

import java.util.HashMap;

/**
 * Class representing the Mongo Update interface.
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
        HashMap<String, Object> find, update;

    }

    private Config config;
    private String collection;
    private Params params;

    /**
     * Create an instance of the Mongo Update Interface.
     * @param config
     * @param collection
     */
    public Update(Config config, String collection) {
        this.config = config;
        this.collection = collection;
        this.params = new Params();
        this.params.update = new HashMap<>();
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
        this.params.update.put("$set", obj);
        return this;
    }

    /**
     * Adds an item to an array.
     * @param obj The object containing items to be added to array.
     * @return
     * <pre>
     * HashMap fields = new HashMap<>();
     * fields.put("tags", "Science");
     * db.update("posts").where(whereClause).push(fields).all(listener);
     * </pre>
     */
    public Update push(HashMap<String, Object> obj) {
        this.params.update.put("$push", obj);
        return this;
    }

    /**
     * Removes the specified field from a document.
     * @param fields The fields to remove.
     * @return
     * <pre>
     * db.update("posts").where(whereClause).remove("tags", "cat").all(listener);
     * </pre>
     */
    public Update remove(String... fields) {
        HashMap map = new HashMap<String, String>();
        for (String field : fields) {
            map.put(field, "");
        }
        this.params.update.put("$unset", map);
        return this;
    }

    /**
     * Renames the specified field.
     * @param obj The object containing fields to rename.
     * @return
     * <pre>
     * HashMap fields = new HashMap<>();
     * fields.put("tags", "categories");
     * db.update("posts").where(whereClause).rename(fields).all(listener);
     * </pre>
     */
    public Update rename(HashMap<String, Object> obj) {
        this.params.update.put("$rename", obj);
        return this;
    }

    /**
     * Increments the value of the field by the specified amount.
     * @param obj The object containing fields to increment along with the value.
     * @return
     * <pre>
     * HashMap fields = new HashMap<>();
     * fields.put("likes", 1);
     * db.update("posts").where(whereClause).inc(fields).all(listener);
     * </pre>
     */
    public Update inc(HashMap<String, Object> obj) {
        this.params.update.put("$inc", obj);
        return this;
    }

    /**
     * Only updates the field if the specified value is greater than the existing field value.
     * @param obj The object containing fields to set.
     * @return
     * <pre>
     * HashMap fields = new HashMap<>();
     * fields.put("likes", 10);
     * db.update("posts").where(whereClause).max(fields).all(listener);
     * </pre>
     */
    public Update max(HashMap<String, Object> obj) {
        this.params.update.put("$max", obj);
        return this;
    }

    /**
     * Only updates the field if the specified value is lesser than the existing field value.
     * @param obj The object containing fields to set.
     * @return
     * <pre>
     * HashMap fields = new HashMap<>();
     * fields.put("likes", 10);
     * db.update("posts").where(whereClause).min(fields).all(listener);
     * </pre>
     */
    public Update min(HashMap<String, Object> obj) {
        this.params.update.put("$min", obj);
        return this;
    }

    /**
     * Multiplies the value of the field by the specified amount.
     * @param obj The object containing fields to multiply along with the value.
     * @return
     * <pre>
     * HashMap fields = new HashMap<>();
     * fields.put("score", 2);
     * db.update("posts").where(whereClause).mul(fields).all(listener);
     * </pre>
     */
    public Update mul(HashMap<String, Object> obj) {
        this.params.update.put("$mul", obj);
        return this;
    }

    /**
     * Sets the value of a field to current timestamp.
     * @param fields The object containing fields to set.
     * @return
     * <pre>
     * db.update("posts").where(whereClause).currentTimestamp("lastUpdated").all(listener);
     * </pre>
     */
    public Update currentTimestamp(String... fields) {
        HashMap map = this.params.update.containsKey("$currentDate") ? (HashMap) this.params.update.get("$currentDate") : new HashMap<String, Object>();
        for (String field : fields) {
            HashMap temp = new HashMap<String, String>();
            temp.put("$type", "timestamp");
            map.put(field, temp);
        }
        this.params.update.put("$currentDate", map);
        return this;
    }

    /**
     * Sets the value of a field to current date.
     * @param fields The object containing fields to set.
     * @return
     * <pre>
     * db.update("posts").where(whereClause).currentDate("createdAt").all(listener);
     * </pre>
     */
    public Update currentDate(String... fields) {
        HashMap map = this.params.update.containsKey("$currentDate") ? (HashMap) this.params.update.get("$currentDate") : new HashMap<String, Object>();
        for (String field : fields) {
            HashMap temp = new HashMap<String, String>();
            temp.put("$type", "date");
            map.put(field, temp);
        }
        this.params.update.put("$currentDate", map);
        return this;
    }

    /**
     * Makes the query to update a single document which matches first.
     * @param listener Listener to listen to the response of update query.
     * <pre>
     * db.update("posts").where(whereClause).set(fields).one(listener);
     * </pre>
     */
    public void one(Utils.ResponseListener listener) {
        this.params.op = "one";
        Utils.fetch(this.config.client,"put", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);

    }

    /**
     * Makes the query to update all documents which matches.
     * @param listener Listener to listen to the response of update query.
     * <pre>
     * db.update("posts").where(whereClause).set(fields).all(listener);
     * </pre>
     */
    public void all(Utils.ResponseListener listener) {
        this.params.op = "all";
        Utils.fetch(this.config.client,"put", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);

    }

    /**
     * Makes the query to update all, else insert a document.
     * @param listener Listener to listen to the response of update query.
     * <pre>
     * db.update("posts").where(whereClause).set(fields).upsert(listener);
     * </pre>
     */
    public void upsert(Utils.ResponseListener listener) {
        this.params.op = "upsert";
        Utils.fetch(this.config.client,"put", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);

    }

}
