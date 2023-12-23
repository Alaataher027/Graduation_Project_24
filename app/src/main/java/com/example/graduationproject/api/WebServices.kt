package com.example.graduationproject.api

import com.example.graduationproject.api.model.login.LoginResponse
import com.example.graduationproject.api.model.login.LoginResponse2
import com.example.graduationproject.api.model.register.RegisterResponse
import com.example.graduationproject.api.model.register.RegisterResponse2
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface WebServices {
    //https://alshaerawy.aait-sa.com/api/ >> base
    // auth/user/register >> end point

    @FormUrlEncoded
    @POST("auth/user/register")
    @Headers("X-Requested-With:XMLHttpRequest")
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
        @Field("phone_number") phone_number: String,
        @Field("user_type") user_type: String,
        @Field("lang") lang: String
        ): Call<RegisterResponse2>

    //https://alshaerawy.aait-sa.com/api/
    // auth/user/login
    @FormUrlEncoded
    @POST("auth/user/login")
    @Headers("X-Requested-With:XMLHttpRequest")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("lang") lang: String,

    ): Call<LoginResponse2>
}