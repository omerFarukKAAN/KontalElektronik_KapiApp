package com.example.kontalelektronik_kapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RESTInterface {

    @GET("nodejs")
    Call<List<UserInformations>> getPosts();

    @POST("nodejs")
    Call<UserInformations> createPost(@Body UserInformations userInfo);
}
