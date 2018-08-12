package com.example.thu.realtysocial.model;

import java.util.Map;

public class PostNews {
    private String typeNews;
    private String content;
    private String address;
    private Double price;
    private String date;
    private long time;


    public PostNews() {
    }

    public PostNews(String typeNews, String content, String address, Double price, String date,long time) {
        this.typeNews = typeNews;
        this.content = content;
        this.address = address;
        this.price = price;
        this.date = date;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTypeNews() {
        return typeNews;
    }

    public void setTypeNews(String typeNews) {
        this.typeNews = typeNews;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
