package com.example.cuadernodelprofesor.database.bodies;

import com.google.gson.annotations.SerializedName;

public class LoginBody {

    @SerializedName("user")
    private String user;

    @SerializedName("pass")
    private String pass;

    public LoginBody(String user,String pass) {
        this.user = user;
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }
}
