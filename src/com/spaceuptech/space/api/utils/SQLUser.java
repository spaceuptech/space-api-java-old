package com.spaceuptech.space.api.utils;

/**
 * Class representing user object for SQL Databases
 */
public class SQLUser {
    public String id;
    public String email;
    public  String name;
    public  String role;

    /**
     * @param id unique id of user.
     * @param email email of user.
     * @param name name of the user.
     * @param role role of the user.
     */
    public SQLUser(String id, String email, String name, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "email: " + email + " id: "  + id + " name: " + name + " role: " + role;
    }
}
