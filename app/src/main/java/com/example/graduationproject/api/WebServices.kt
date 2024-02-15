package com.example.graduationproject.api

import com.example.graduationproject.api.model.CheckCodeResponse
import com.example.graduationproject.api.model.ForgetPasswordResponse
import com.example.graduationproject.api.model.logout.LogOutResponse
import com.example.graduationproject.api.model.ResetPasswordResponse
import com.example.graduationproject.api.model.login.LoginResponse2
import com.example.graduationproject.api.model.register.RegisterResponse2
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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


    //https://alshaerawy.aait-sa.com/api/
    // password/email
    @FormUrlEncoded
    @POST("password/email")
    fun forgetPassword(
        @Field("email") email: String,
        @Field("lang") lang: String,

        ): Call<ForgetPasswordResponse>


    //https://alshaerawy.aait-sa.com/api/
    // password/code/check
    @FormUrlEncoded
    @POST("password/code/check")
    fun checkCode(
        @Field("code") code: String,
        @Field("lang") lang: String,

        ): Call<CheckCodeResponse>

    //https://alshaerawy.aait-sa.com/api/password/reset
    @FormUrlEncoded
    @POST("password/reset")
    fun resetPassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
        @Field("lang") lang: String,

        ): Call<ResetPasswordResponse>

    //https://alshaerawy.aait-sa.com/api/
    // auth/user/logout
    @POST("auth/user/logout")
    fun logOut(
        @Header("Authorization") accessToken: String
    ): Call<LogOutResponse>


//    @GET("auth/user-profile")
//    fun getUserProfile(
//        @Header("Authorization") accessToken: String
//    ): Call<UserProfileResponse>

}