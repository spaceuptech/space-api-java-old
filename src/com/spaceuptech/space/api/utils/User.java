package com.spaceuptech.space.api.utils;

public class User {
    public String id;
    public String email;
//    public  String name;
    public  String role;

    public User(String id, String email, String role) {
        this.id = id;
        this.email = email;
//        this.name = name;
        this.role = role;
    }
}
