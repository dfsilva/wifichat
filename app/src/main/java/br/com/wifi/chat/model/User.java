package br.com.wifi.chat.model;

import com.google.gson.Gson;

public class User {

    public final String name;

    public User(String name) {
        this.name = name;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static User fromJson(String json) {
        return new Gson().fromJson(json, User.class);
    }
}
