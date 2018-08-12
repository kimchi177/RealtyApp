package com.example.thu.realtysocial.Retrofit.ClassData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("username")
@Expose
private String username;
@SerializedName("email")
@Expose
private String email;
@SerializedName("briday")
@Expose
private String briday;
@SerializedName("imageAvartar")
@Expose
private String imageAvartar;
@SerializedName("phone_number")
@Expose
private String phoneNumber;
@SerializedName("sex")
@Expose
private String sex;
@SerializedName("starHate")
@Expose
private Double starHate;
@SerializedName("starLove")
@Expose
private Double starLove;
@SerializedName("status")
@Expose
private String status;
@SerializedName("address")
@Expose
private String address;
@SerializedName("dateSetUp")
@Expose
private String dateSetUp;
@SerializedName("device_token")
@Expose
private String deviceToken;
@SerializedName("typeUser")
@Expose
private String typeUser;

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
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

public String getBriday() {
return briday;
}

public void setBriday(String briday) {
this.briday = briday;
}

public String getImageAvartar() {
return imageAvartar;
}

public void setImageAvartar(String imageAvartar) {
this.imageAvartar = imageAvartar;
}

public String getPhoneNumber() {
return phoneNumber;
}

public void setPhoneNumber(String phoneNumber) {
this.phoneNumber = phoneNumber;
}

public String getSex() {
return sex;
}

public void setSex(String sex) {
this.sex = sex;
}

public Double getStarHate() {
return starHate;
}

public void setStarHate(Double starHate) {
this.starHate = starHate;
}

public Double getStarLove() {
return starLove;
}

public void setStarLove(Double starLove) {
this.starLove = starLove;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public String getDateSetUp() {
return dateSetUp;
}

public void setDateSetUp(String dateSetUp) {
this.dateSetUp = dateSetUp;
}

public String getDeviceToken() {
return deviceToken;
}

public void setDeviceToken(String deviceToken) {
this.deviceToken = deviceToken;
}

public String getTypeUser() {
return typeUser;
}

public void setTypeUser(String typeUser) {
this.typeUser = typeUser;
}

}