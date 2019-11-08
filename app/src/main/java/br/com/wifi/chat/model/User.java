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
}
