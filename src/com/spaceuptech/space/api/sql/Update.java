package com.spaceuptech.space.api.sql;

import com.google.gson.Gson;
import com.spaceuptech.space.api.mongo.Mongo;
import com.spaceuptech.space.api.utils.And;
import com.spaceuptech.space.api.utils.Condition;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

import java.util.HashMap;

public class Update {
    private class Params {
        String op;
        HashMap<String, Object> find, record;
    }

    private Config config;
    private String table, db;
    private Params params;

    public Update(String db, Config config, String table) {
        this.db = db;
        this.config = config;
        this.table = table;
        this.params = new Params();
    }

    public Update where(Condition... conds) {
        if (conds.length == 1) this.params.find = Mongo.generateFind(conds[0]);
        else this.params.find = Mongo.generateFind(And.create(conds));
        return this;
    }

    public Update push(HashMap<String, Object> obj) {
        this.params.record = obj;
        return this;
    }

    public void one(Utils.ResponseListener listener) {
        this.params.op = "one";
        Utils.fetch(this.config.client,"put", this.config.token,
                SQL.sqlURL(this.config.url, this.db, this.config.projectId, this.table, ""),
                new Gson().toJson(this.params), listener);

    }

    public void all(Utils.ResponseListener listener) {
        this.params.op = "all";
        Utils.fetch(this.config.client,"put", this.config.token,
                SQL.sqlURL(this.config.url, this.db, this.config.projectId, this.table, ""),
                new Gson().toJson(this.params), listener);

    }
}
