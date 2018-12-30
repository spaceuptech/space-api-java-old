package com.spaceuptech.space.api.utils;

/**
 * Class representing user object for MongoDB Database
 */
public class MongoUser {
    public String _id;
    public String email;
    public  String name;
    public  String role;

    /**
     * @param _id unique id of the user.
     * @param email email of the user.
     * @param name name of the user
     * @param role role of the user.
     */
    public MongoUser(String _id, String email, String name, String role) {
        this._id = _id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "email: " + email + " id: "  + _id + " name: " + name + " role: " + role;
    }
}
