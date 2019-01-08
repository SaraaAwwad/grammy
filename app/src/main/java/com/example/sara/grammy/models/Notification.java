package com.example.sara.grammy.models;

public class Notification {

    private String user_id;
    private String photo_id;
    private String image_path;

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

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return "Like{" +
                "user_id='" + user_id + '\'' +
                "photo_id='" + photo_id + '\'' +
                "image_path='" + image_path + '\'' +
                '}';
    }
}
