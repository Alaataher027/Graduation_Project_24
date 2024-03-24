package com.example.graduationproject.api

import com.example.graduationproject.api.model.CheckCodeResponse
import com.example.graduationproject.api.model.ForgetPasswordResponse
import com.example.graduationproject.api.model.logout.LogOutResponse
import com.example.graduationproject.api.model.ResetPasswordResponse
import com.example.graduationproject.api.model.login.LoginResponse2
import com.example.graduationproject.api.model.profile.ProfileResponse
import com.example.graduationproject.api.model.register.RegisterResponse2
import com.example.graduationproject.api.model.editProfile.EditProfileResponse
import com.example.graduationproject.api.model.imageProfile.ImageProfileResponse
import com.example.graduationproject.api.model.login.loginGoogle.LoginGoogleResponse
import com.example.graduationproject.api.model.post.createPost.PostResponse
import com.example.graduationproject.api.model.post.deletePost.DeletePostResponse
import com.example.graduationproject.api.model.post.editPost.EditPostResponse
import com.example.graduationproject.api.model.post.postHome.HomePostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


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

    @GET("login/google")
    fun loginWithGoogle(): Call<LoginGoogleResponse>


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

    //https://alshaerawy.aait-sa.com/api/
    // auth/user/user-profile
    @POST("auth/user/user-profile/{id}")
    fun getUserProfile(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ): Call<ProfileResponse>


    @PUT("auth/user/edit")
    fun editEmail(
        @Header("Authorization") accessToken: String,
        @Query("email") email: String
    ): Call<EditProfileResponse>

    @PUT("auth/user/edit")
    fun editPhone(
        @Header("Authorization") accessToken: String,
        @Query("phone_number") phoneNumber: String
    ): Call<EditProfileResponse>

    @PUT("auth/user/edit")
    fun editGovernorate(
        @Header("Authorization") accessToken: String,
        @Query("governorate") governorate: String
    ): Call<EditProfileResponse>

    @PUT("auth/user/edit")
    fun editCity(
        @Header("Authorization") accessToken: String,
        @Query("city") city: String
    ): Call<EditProfileResponse>

    @PUT("auth/user/edit")
    fun editSellerStreet(
        @Header("Authorization") accessToken: String,
        @Query("street") street: String
    ): Call<EditProfileResponse>

    @PUT("auth/user/edit")
    fun editSellerQuarter(
        @Header("Authorization") accessToken: String,
        @Query("residential_quarter") residentialQuarter: String
    ): Call<EditProfileResponse>

    @PUT("auth/user/edit")
    fun editTAXNumber(
        @Header("Authorization") accessToken: String,
        @Query("TIN") tIN: String
    ): Call<EditProfileResponse>

    @Multipart
    @POST("auth/user/profileimg")
    fun imageProfile(
        @Header("Authorization") accessToken: String,
        @Part image: MultipartBody.Part
    ): Call<ImageProfileResponse>

    @Multipart
    @POST("posts/add")
    fun createPost(
        @Header("Authorization") accessToken: String,
        @Part("description") description: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("material") material: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<PostResponse>

    @GET("posts/home")
    fun getHomePosts(
        @Header("Authorization") accessToken: String
    ): Call<HomePostResponse>


    //https://alshaerawy.aait-sa.com/api/posts/{id}
    @POST("posts/{id}")
    fun deletePost(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ): Call<DeletePostResponse>


    //https://alshaerawy.aait-sa.com/api/
    // posts/{id}/edit
    @PUT("posts/{id}/edit")
    fun editPostImage(
        @Header("Authorization") accessToken: String,
        @Query("image") image: String,
        @Path("id") id: Int
    ): Call<EditPostResponse>

    @PUT("posts/{id}/edit")
    fun editPostDescription(
        @Header("Authorization") accessToken: String,
        @Query("description") description: String,
        @Path("id") id: Int
    ): Call<EditPostResponse>

    @PUT("posts/{id}/edit")
    fun editPostPrice(
        @Header("Authorization") accessToken: String,
        @Query("price") price: String,
        @Path("id") id: Int
    ): Call<EditPostResponse>

    @PUT("posts/{id}/edit")
    fun editPostMaterial(
        @Header("Authorization") accessToken: String,
        @Query("material") material: String,
        @Path("id") id: Int
    ): Call<EditPostResponse>

    @PUT("posts/{id}/edit")
    fun editPostQuantity(
        @Header("Authorization") accessToken: String,
        @Query("quantity") quantity: String,
        @Path("id") id: Int
    ): Call<EditPostResponse>


}