package com.mlab.contactorganizer.obj;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by labud on 02.03.2018.
 */

public class UserObj implements Serializable {

    private String id;
    private String name;
    private String token;
    private String email;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String userId) {
        this.id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
