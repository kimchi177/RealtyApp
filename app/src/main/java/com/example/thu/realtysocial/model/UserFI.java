package com.example.thu.realtysocial.model;

public class UserFI {

    private String user_id;
    private String username;
    private String email;
    private String phone_number;
    private String Sex;
    private String imageAvartar;
    private String imageCover;
    private String briday;
    private String typeUser;
    private String address;
    private String status;
    private String device_token;
    private double starLove;
    private double starHate;
    private String DateSetUp;

    public UserFI() {
    }

    public UserFI(String user_id, String username, String email, String phone_number, String sex, String imageAvartar, String imageCover, String briday, String typeUser, String address, String status,
                String device_token, double starLove, double starHate, String dateSetUp) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.phone_number = phone_number;
        Sex = sex;
        this.imageAvartar = imageAvartar;
        this.imageCover = imageCover;
        this.briday = briday;
        this.typeUser = typeUser;
        this.address = address;
        this.status = status;
        this.device_token = device_token;
        this.starLove = starLove;
        this.starHate = starHate;
        DateSetUp = dateSetUp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getImageAvartar() {
        return imageAvartar;
    }

    public void setImageAvartar(String imageAvartar) {
        this.imageAvartar = imageAvartar;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public String getBriday() {
        return briday;
    }

    public void setBriday(String briday) {
        this.briday = briday;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public double getStarLove() {
        return starLove;
    }

    public void setStarLove(double starLove) {
        this.starLove = starLove;
    }

    public double getStarHate() {
        return starHate;
    }

    public void setStarHate(double starHate) {
        this.starHate = starHate;
    }

    public String getDateSetUp() {
        return DateSetUp;
    }

    public void setDateSetUp(String dateSetUp) {
        DateSetUp = dateSetUp;
    }
}