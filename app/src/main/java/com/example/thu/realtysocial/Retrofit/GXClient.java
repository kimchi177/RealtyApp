package com.example.thu.realtysocial.Retrofit;



import com.example.thu.realtysocial.Retrofit.ClassData.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GXClient {

    private static GXClient sGxClient;
    public static GXClient getInstance() {
        if (sGxClient == null) {
            sGxClient = new GXClient();
        }
        return sGxClient;
    }
    GxApiInterface dataCLient = GxApiClient.getGxApiClient();
    public void InsertData(String user_id, String username, String email, String briday, String imageAvartar, String phone_number, String sex,
                           Double starHate, Double starLove,
                           String status, String address, String dateSetUp,
                           String device_token, String typeUser, Callback<List<User>> callback){
        Call<List<User>> call = dataCLient.InsertData(user_id,username,email,briday,imageAvartar,phone_number,sex,starHate,starLove,status,address,dateSetUp
        ,device_token,typeUser);
        call.enqueue(callback);
    }
}