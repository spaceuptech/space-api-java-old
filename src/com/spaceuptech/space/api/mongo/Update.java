package com.spaceuptech.space.api.mongo;

import com.google.gson.Gson;
import com.spaceuptech.space.api.utils.And;
import com.spaceuptech.space.api.utils.Condition;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

import java.util.HashMap;

public class Update {

    private class Params {
        String op;
        HashMap<String, Object> find, $set;

    }

    private Config config;
    private String collection;
    private Params params;

    public Update(Config config, String collection) {
        this.config = config;
        this.collection = collection;
        this.params = new Params();
    }

    public Update where(Condition... conds) {
        if (conds.length == 1) this.params.find = Mongo.generateFind(conds[0]);
        else this.params.find = Mongo.generateFind(And.create(conds));
        return this;
    }

    public Update set(HashMap<String, Object> obj) {
        this.params.$set = obj;
        return this;
    }

    public void one(Utils.ResponseListener listener) {
        this.params.op = "one";
        Utils.fetch(this.config.client,"put", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);

    }

    public void all(Utils.ResponseListener listener) {
        this.params.op = "all";
        Utils.fetch(this.config.client,"put", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);

    }

    public void upsert(Utils.ResponseListener listener) {
        this.params.op = "upsert";
        Utils.fetch(this.config.client,"put", this.config.token,
                Mongo.mongoURL(this.config.url, this.config.projectId, this.collection, ""),
                new Gson().toJson(this.params), listener);

    }

}
