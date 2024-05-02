package com.example.graduationproject.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.StoreFCMTokenResponse
import com.example.graduationproject.api.model.login.Data
import com.example.graduationproject.api.model.login.ErrorResponse
import com.example.graduationproject.api.model.login.LoginResponse
import com.example.graduationproject.api.model.login.LoginResponse2
import com.example.graduationproject.api.model.login.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val tokenManager: TokenManager) : ViewModel() {

    fun performLogin(
        email: String,
        password: String,
        lang: String,
        onLoginResult: (Boolean, String) -> Unit
    ) {
        ApiManager.getApis()
            .userLogin(email, password, lang)
            .enqueue(object : Callback<LoginResponse2> {
                override fun onResponse(
                    call: Call<LoginResponse2>,
                    response: Response<LoginResponse2>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse: LoginResponse2? = response.body()
                        if (loginResponse != null) {
                            val status: Int? = loginResponse.status
                            val message: String? = loginResponse.message
                            if (status == 200) {
                                val data: Data? = loginResponse.data
                                val accessToken: String? = data?.accessToken
                                val id: Int? = loginResponse.data?.user?.id
                                val userType: String? = data?.user?.userType
                                if (!accessToken.isNullOrBlank()) {
                                    tokenManager.saveToken(accessToken)
                                    tokenManager.saveUserId(id ?: 0)
                                    tokenManager.saveUserType(userType ?: "")
                                    // Send FCM token to server
                                    sendFCMTokenToServer()

                                    onLoginResult(true, message ?: "")
                                } else {
                                    onLoginResult(false, "Invalid access token")
                                }
                            } else {
                                onLoginResult(false, message ?: "Login failed")
                            }
                        } else {
                            onLoginResult(false, "Unexpected error occurred")
                        }
                    } else {
                        val json = response.errorBody()?.charStream()
                        Log.e("Tag", "$json")
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        onLoginResult(
                            false,
                            "${Gson().fromJson<ErrorResponse>(json, type).message}"
                        )
                    }
                }

                override fun onFailure(call: Call<LoginResponse2>, t: Throwable) {
                    onLoginResult(false, "Network error: ${t.message}")
                    Log.d("Login","msg: ${t.message}" )
                }
            })
    }

    private fun sendFCMTokenToServer() {
        val fcmToken = tokenManager.getFCMToken()
        val accessToken = tokenManager.getToken()
        if (!fcmToken.isNullOrBlank() && !accessToken.isNullOrBlank()) {
            ApiManager.getApisToken(accessToken).sendFCMTokenToServer(accessToken, fcmToken)
                .enqueue(object : Callback<StoreFCMTokenResponse> {
                    override fun onResponse(
                        call: Call<StoreFCMTokenResponse>,
                        response: Response<StoreFCMTokenResponse>
                    ) {
                        val fcmResponse = response.body()
                        val FCM: String? = fcmResponse?.data?.fcmToken

                        if (response.isSuccessful) {
                            // Check if status is 200
                            if (fcmResponse?.status == 200) {
                                Log.d("LoginViewModel", "FCM token sent successfully : ${fcmToken}")
                                Log.d("LoginViewModel", "FCM token sent successfully : ${FCM}")
                            } else {
                                Log.e("LoginViewModel", "Failed to send FCM token: ${fcmResponse?.message}")
                            }
                        } else {
                            Log.e("LoginViewModel", "Failed to send FCM token: ${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<StoreFCMTokenResponse>, t: Throwable) {
                        Log.e(
                            "LoginViewModel",
                            "Network error while sending FCM token: ${t.message}"
                        )
                    }
                })
        } else {
            Log.e("LoginViewModel", "FCM token or access token is null or blank")
        }
    }

}

// coroutiens:

//{
//    override fun onResponse(
//        call: Call<LoginResponse>,
//        response: Response<LoginResponse>
//    ) {
//
//        Log.d("LoginViewModel", "Response: ${response.code()}, ${response.body()}")
//        if (response.isSuccessful) {
//            onLoginResult(true, "Login successful")
//        } else {
//            val errorResponse = response.errorBody()?.string()
//            if (errorResponse != null) {
//                val errorResultObject =
//                    Gson().fromJson(errorResponse, LoginResponse::class.java)
//                val errorMessage =
//                    "Login failed: ${errorResultObject.message}\n" +
//                            "Email: ${errorResultObject.loginErrors?.email}\n" +
//                            "Password: ${errorResultObject.loginErrors?.password}\n"
//                Log.d("LoginViewModel", "Error Response: $errorResponse")
//                onLoginResult(false, errorMessage)
//
//            }
//        }
//    }
//    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//        onLoginResult(false, "Login failed: ${t.message}")
//    }
//}