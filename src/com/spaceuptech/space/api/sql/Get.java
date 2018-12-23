package com.spaceuptech.space.api.sql;

import com.google.gson.Gson;
import com.spaceuptech.space.api.mongo.Mongo;
import com.spaceuptech.space.api.utils.And;
import com.spaceuptech.space.api.utils.Condition;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;

import java.util.HashMap;

public class Get {
    private class Params {
        HashMap<String, Object> find;
        int offset, limit;
        HashMap<String, Integer> sort, select;
        String op;
    }

    private String table, db;
    private Config config;

    private Params params;

    public Get(String db, Config config, String table) {
        this.db = db;
        this.config = config;
        this.table = table;
        this.params = new Params();
    }

    public Get where(Condition... conds) {
        if (conds.length == 1) this.params.find = Mongo.generateFind(conds[0]);
        else this.params.find = Mongo.generateFind(And.create(conds));
        return this;
    }

    public Get select(HashMap<String, Integer> select) {
        this.params.select = select;
        return this;
    }

    public Get sort(String... sort) {
        HashMap map = new HashMap<String, Integer>();
        for (String s : sort) {
            map.put(s, s.startsWith("-") ? -1: 1);
        }
        this.params.sort = map;
        return this;
    }

    public Get offset(int offset) {
        this.params.offset = offset;
        return this;
    }

    public Get limit(int limit) {
        this.params.limit = limit;
        return this;
    }

    public void one(Utils.ResponseListener listener) {
        this.params.op = "one";

        Utils.fetch(this.config.client,"get", this.config.token, this.getUrl(), "", listener);
    }

    public void all(Utils.ResponseListener listener) {
        this.params.op = "all";

        Utils.fetch(this.config.client,"get", this.config.token, this.getUrl(), "", listener);
    }


    private String getUrl() {
        Gson gson = new Gson();

        String params = "op=" + this.params.op;
        params += "&find=" + gson.toJson(this.params.find);
        if (this.params.select != null && this.params.select.keySet().size() > 0) params += "&select=" + gson.toJson(this.params.select);
        if (this.params.sort != null && this.params.sort.keySet().size() > 0) params += "&sort=" + gson.toJson(this.params.sort);
        if (this.params.limit > 0) params += "&limit=" + this.params.limit;
        if (this.params.offset > 0) params += "&skip=" + this.params.offset;
        return SQL.sqlURL(this.config.url, this.db, this.config.projectId, this.table, params);
    }
}
