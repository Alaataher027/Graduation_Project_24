package com.example.graduationproject.api

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.graduationproject.api.model.LoginResponse
import com.example.graduationproject.api.model.RegisterResponse
import com.example.graduationproject.data.User
import com.example.graduationproject.ui.ForgetPassword
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.QueryName

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
        @Field("phone_number") phone_number: String
    ): Call<RegisterResponse>

    //https://alshaerawy.aait-sa.com/api/
    // auth/user/login
    @FormUrlEncoded
    @POST("auth/user/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String,

        ): Call<LoginResponse>
}