package com.shuvam.basicfirebase.model;

/**
 * Created by Shuvam Ghosh on 8/7/2017.
 */

public class User {

    public String username;

    public User(String uname){
        this.username = uname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
