package com.spaceuptech;

import com.spaceuptech.space.api.API;
import com.spaceuptech.space.api.mongo.Mongo;
import com.spaceuptech.space.api.sql.SQL;
import com.spaceuptech.space.api.utils.*;

import java.util.HashMap;


public class Main {

    private static Utils.ResponseListener generateListner(String event) {
        Utils.ResponseListener listener = new Utils.ResponseListener() {
            @Override
            public void onResponse(int statusCode, Object data) {
                System.out.println(event + ":: StatusCode: " + statusCode + " Data: " + data.toString());
            }

            @Override
            public void onError(Exception e) {
                System.out.println(event + ":: Error: " + e.toString());
            }
        };
        return  listener;
    }


    public static void main(String[] args) {
        API api = new API("realtime-mysql", "http://localhost:8080");
        SQL mySQL = api.MySQL();
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InVzZXIxQGdtYWlsLmNvbSIsImlkIjoiYTNjMTdkODMtMDVjMC0xMWU5LWJiYTgtMmM2MDBjYzZlNTUyIiwicm9sZSI6InVzZXIifQ.XrzX0s3kWPSTJq39BaLjNQajeOzmnozpzupwaesYAY0";
        Utils.SQLAuthListener signUpListener = new Utils.SQLAuthListener() {
            @Override
            public void onResponse(int statusCode, AuthResponse res) {
                System.out.println("SignUp Response: " + res.toString());
            }

            @Override
            public void onError(Exception e) {
                System.out.println("Error: " + e.toString());
            }
        };
        mySQL.signUp("user1@gmail.com", "User 1", "123", "user", signUpListener);

//        Utils.ResponseListener signInListener = new Utils.ResponseListener() {
//            @Override
//            public void onResponse(int statusCode, Object data) {
//                System.out.println("SignIn StatusCode: " + statusCode + " Data: " + data.toString());
//            }
//
//            @Override
//            public void onError(Exception e) {
//                System.out.println("SignIn  Error: " + e.toString());
//            }
//        };
//        mongo.signIn("user1@gmail.com","123", generateListner("Signin"));

//        api.setToken(token);


//        Utils.ResponseListener insertOneListener = new Utils.ResponseListener() {
//            @Override
//            public void onResponse(int statusCode, Object data) {
//                System.out.println("Insert StatusCode: " + statusCode + " Data: " + data.toString());
//            }
//
//            @Override
//            public void onError(Exception e) {
//                System.out.println("Insert  Error: " + e.toString());
//            }
//        };
//
//        mongo.insert("posts").one(new Post("Post 1", "user1@gmail.com", "This is a good post"), insertOneListener);
//
//        Utils.ResponseListener insertManyListener = new Utils.ResponseListener() {
//            @Override
//            public void onResponse(int statusCode, Object data) {
//                System.out.println("Insert Many StatusCode: " + statusCode + " Data: " + data.toString());
//            }
//
//            @Override
//            public void onError(Exception e) {
//                System.out.println("Insert Many Error: " + e.toString());
//            }
//        };
//
//        Post[] posts = new Post[2];
//        posts[0] = new Post("Post 2", "user1@gmail.com", "This is a better post");
//        posts[1] = new Post("Post 3", "user2@gmail.com", "This post is excellent");
//        mongo.insert("posts").all(posts, insertManyListener);

//        mongo.get("posts").count(generateListner("Get Count"));
//        mongo.get("posts").one(generateListner("Get One"));
//        mongo.get("posts").all(generateListner("Get All"));
//        Or and = Or.create(Cond.create("author", "==", "user1@gmail.com"), Cond.create("title", "==", "Post 3"));
//        mongo.get("posts").where(and).all(generateListner("Get All Where"));
//        mongo.get("posts").where(new Cond("author", "==", "user1@gmail.com")).limit(1).all(generateListner("Get All Limit"));
//        mongo.get("posts").sort("title").all(generateListner("Get All Sort"));
//        HashMap select = new HashMap<String, Integer>();
//        select.put("_id", 0);
//        mongo.get("posts").skip(1).limit(1).select(select).all(generateListner("Get Skip"));
//        mongo.get("posts").distinct("author", generateListner("Get Distinct"));
//        HashMap map = new HashMap<String, Object>();
//        map.put("content", "This post is best");
//        mongo.update("posts")
//                .where(new Cond("title", "==", "Post 3"))
//                .set(map).one(generateListner("Update All"));
//        mongo.profile("a3c17d83-05c0-11e9-bba8-2c600cc6e552",generateListner("Profile"));
//
//        mongo.profiles(generateListner("Profile"));
//        mongo.editProfile("a3c17d83-05c0-11e9-bba8-2c600cc6e552", "user1@gmail.com", "Jayesh", "123", generateListner("Edit Profile"));

//        api.call("my-engine", "my-func", 5000, "Faas is awesome!", generateListner("FaaS"));

    }
}
