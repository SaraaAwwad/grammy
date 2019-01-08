package com.example.sara.grammy.models;

public class Notification {

    private String user_id;

    public Notification(String userid) {
        this.user_id = userid;
    }

    public Notification() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Like{" +
                "user_id='" + user_id + '\'' +
                '}';
    }
}
