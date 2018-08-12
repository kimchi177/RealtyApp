package com.example.thu.realtysocial.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Friend {
    public String date;

    public Friend() {
    }

    public Friend(String date) {
        this.date = date;
    }
    @Exclude
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
