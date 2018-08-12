package com.example.thu.realtysocial.Retrofit;


import com.example.thu.realtysocial.Retrofit.ClassData.User;
import com.example.thu.realtysocial.Retrofit.ResultInsert.ResultCheckCodePro;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GxApiInterface {
    @Multipart
    @POST("uploadImage.php")
    Call<String> uploadImage(@Part MultipartBody.Part photo);

    //    @FormUrlEncoded
//    @POST("Insert.php")
//    Call<String> InsertData(@Field("taikhoan") String taiKhoan,
//                            @Field("matkhau") String matKhau,
//                            @Field("hinhanh") String hinhAnh);
    @FormUrlEncoded
    @POST("InsertUsers.php")
    Call<List<User>> InsertData(@Field("user_id") String user_id,
                                @Field("username") String username,
                                @Field("email") String email,
                                @Field("briday") String briday,
                                @Field("imageAvartar") String imageAvartar,
                                @Field("phone_number") String phone_number,
                                @Field("sex") String sex,
                                @Field("starHate") Double starHate,
                                @Field("starLove") Double starLove,
                                @Field("status") String status,
                                @Field("address") String address,
                                @Field("dateSetUp") String dateSetUp,
                                @Field("device_token") String device_token,
                                @Field("typeUser") String typeUser
    );

//    @FormUrlEncoded
//    @POST("Login.php")
//    Call<List<SinhVien>> LoginData(@Field("taikhoan") String taikhoan,
//                                   @Field("matkhau") String matkhau
//    );
//
//    @GET("Delete.php")
//    Call<String> Delete(@Query("id") String id,
//                        @Query("hinhanh") String hinhanh);
}
