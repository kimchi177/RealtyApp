package com.example.thu.realtysocial.Retrofit;

public class GxApiClient {
    public static  final String baseUrl="https://kimkim27061997.000webhostapp.com/";
//    public static  final String baseUrl="http://192.168.1.120:8080/shopsaleTHU/";
    public static GxApiInterface getGxApiClient()
    {
        return  RetrofitClient.getClient(baseUrl).create(GxApiInterface.class);
    }
}