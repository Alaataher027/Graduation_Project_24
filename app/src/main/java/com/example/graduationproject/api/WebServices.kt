package com.example.graduationproject.api

import com.example.graduationproject.api.model.CheckCodeResponse
import com.example.graduationproject.api.model.ForgetPasswordResponse
import com.example.graduationproject.api.model.LocationResponse
import com.example.graduationproject.api.model.Order2Response
import com.example.graduationproject.api.model.logout.LogOutResponse
import com.example.graduationproject.api.model.ResetPasswordResponse
import com.example.graduationproject.api.model.StoreFCMTokenResponse
import com.example.graduationproject.api.model.login.LoginResponse2
import com.example.graduationproject.api.model.profile.ProfileResponse
import com.example.graduationproject.api.model.register.RegisterResponse2
import com.example.graduationproject.api.model.editProfile.EditProfileResponse
import com.example.graduationproject.api.model.imageProfile.ImageProfileResponse
import com.example.graduationproject.api.model.login.loginGoogle.LoginGoogleResponse
import com.example.graduationproject.api.model.order.accORrej.AcceptOrRejectOrderResponse
import com.example.graduationproject.api.model.order.sendOrder.OrderResponse
import com.example.graduationproject.api.model.post.createPost.ClassificationResponse
import com.example.graduationproject.api.model.post.createPost.PostResponse
import com.example.graduationproject.api.model.post.deletePost.DeletePostResponse
import com.example.graduationproject.api.model.post.editPost.EditPostResponse
import com.example.graduationproject.api.model.post.postHome.HomePostResponse
import com.example.graduationproject.api.model.post.savePost.SavePostResponse
import com.example.graduationproject.api.model.post.savePost.SavePostsListResponse
import com.example.graduationproject.api.model.search.SearchAddressResponse
import com.example.graduationproject.api.model.notifications.SellerNotificationResponse
import com.example.graduationproject.api.model.post.GetPostByIdResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
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
        @Query("address") address: String
    ): Call<EditProfileResponse>

    @PUT("auth/user/edit")
    fun editSellerStreet(
        @Header("Authorization") accessToken: String,
        @Query("street") street: String
    ): Call<EditProfileResponse>

    @PUT("auth/user/edit")
    fun editSellerQuarter(
        @Header("Authorization") accessToken: String,
        @Query("residentialQuarter") residentialQuarter: String
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
    @DELETE("posts/{id}")
    fun deletePost(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int
    ): Call<DeletePostResponse>


    // function to save a post
    @POST("posts/save-post/{postId}")
    fun savePost(
        @Header("Authorization") accessToken: String,
        @Path("postId") postId: String
    ): Call<SavePostResponse>

    //https://alshaerawy.aait-sa.com/api/posts/saved/list
    @GET("posts/saved/list")
    fun getSavedPosts(@Header("Authorization") accessToken: String): Call<SavePostsListResponse>


    //https://alshaerawy.aait-sa.com/api/
    // posts/{id}/edit
    @Headers("X-Requested-With:XMLHttpRequest")
    @FormUrlEncoded
    @POST("posts/{id}/edit")
    fun editPostImage(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int,
        @Field("image") image: MultipartBody.Part
    ): Call<EditPostResponse>

    //    https://alshaerawy.aait-sa.com/api/posts/%7Bid%7D/edit
    @FormUrlEncoded
    @Headers("X-Requested-With:XMLHttpRequest")
    @POST("posts/{id}/edit")
    fun editPostDescription(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int,
        @Field("description") description: String
    ): Call<EditPostResponse>

    @FormUrlEncoded
    @POST("posts/{id}/edit")
    @Headers("X-Requested-With:XMLHttpRequest")
    fun editPostPrice(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int,
        @Field("price") price: String
    ): Call<EditPostResponse>

    @FormUrlEncoded
    @POST("posts/{id}/edit")
    @Headers("X-Requested-With:XMLHttpRequest")
    fun editPostMaterial(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int,
        @Field("material") material: String
    ): Call<EditPostResponse>

    @FormUrlEncoded
    @POST("posts/{id}/edit")
    @Headers("X-Requested-With:XMLHttpRequest")
    fun editPostQuantity(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int,
        @Field("quantity") quantity: String
    ): Call<EditPostResponse>


    @POST("posts/{id}/edit")
    @Multipart
    fun editPost(
        @Header("Authorization") accessToken: String,
        @Path("id") id: Int,
        @Part("description") description: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("material") material: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<EditPostResponse>

    //https://alshaerawy.aait-sa.com/api/posts/search/{query}
    @GET("posts/search/{query}")
    fun searchByAddress(
        @Header("Authorization") accessToken: String,
        @Path("query") query: String
    ): Call<SearchAddressResponse>

    //https://alshaerawy.aait-sa.com/api/auth/user/store-fcm-token
    @POST("auth/user/store-fcm-token")
    @FormUrlEncoded
    fun sendFCMTokenToServer(
        @Header("Authorization") accessToken: String,
        @Field("token") token: String
    ): Call<StoreFCMTokenResponse>

    @POST("posts/classify")
    @Multipart
    fun classifyImage(
        @Header("Authorization") accessToken: String,
        @Part image: MultipartBody.Part
    ): Call<ClassificationResponse>


    //  add_to_chart => https://alshaerawy.aait-sa.com/api/posts/order/add/ 135 {post_id } / 1 {buyer_id} — POST
    @POST("posts/order/add/{post_id}/{buyer_id}")
    fun orderAndAddToCart(
        @Header("Authorization") accessToken: String,
        @Path("post_id") post_id: String,
        @Path("buyer_id") buyer_id: String
//        @Field("order_expire") order_expire: String
    ): Call<OrderResponse>

    // https://alshaerawy.aait-sa.com/api/auth/user/show/seller/notifications
    @GET("auth/user/show/seller/notifications")
    fun getSellerNotification(
        @Header("Authorization") accessToken: String

    ): Call<SellerNotificationResponse>

    // https://alshaerawy.aait-sa.com/api/posts/order/30 // {order_id}
    @POST("posts/order/{order_id}")
    @FormUrlEncoded
    fun acceptOrRejectOrder(
        @Header("Authorization") accessToken: String,
        @Path("order_id") order_id: String,
        @Field("condition") condition: String
    ): Call<AcceptOrRejectOrderResponse>

    @GET("posts/order/chart/{buyer_id}")
    fun getOrdersForUser(
        @Header("Authorization") accessToken: String,
        @Path("buyer_id") buyerId: Int
    ): Call<Order2Response>

    @GET("posts/{post_id}")
    fun getPostById(
        @Header("Authorization") accessToken: String,
        @Path("post_id") post_id: Int
    ): Call<GetPostByIdResponse>


    @GET("lookups/govs")
    fun getLocationData(): Call<LocationResponse>


}