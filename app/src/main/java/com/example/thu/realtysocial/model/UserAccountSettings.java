package com.example.thu.realtysocial.model;

public class UserAccountSettings {

    private String description;
    private String display_name;
    private String status;
    private String profile_photo;
    private String username;
    private String thum_image;
    private String device_token;
    private String user_id;

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public UserAccountSettings(String description, String display_name, String status, String profile_photo, String username, String thum_image,
                               String device_token, String user_id) {
        this.description = description;
        this.display_name = display_name;
        this.status = status;
        this.profile_photo = profile_photo;
        this.username = username;
        this.thum_image = thum_image;
        this.user_id = user_id;
    }

    public UserAccountSettings() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThum_image() {
        return thum_image;
    }

    public void setThum_image(String thum_image) {
        this.thum_image = thum_image;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "description='" + description + '\'' +
                ", display_name='" + display_name + '\'' +
                ", followers=" + status +
                ", profile_photo='" + profile_photo + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}