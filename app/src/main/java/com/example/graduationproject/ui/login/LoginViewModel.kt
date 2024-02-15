package com.example.graduationproject.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.login.Data
import com.example.graduationproject.api.model.login.ErrorResponse
import com.example.graduationproject.api.model.login.LoginResponse
import com.example.graduationproject.api.model.login.LoginResponse2
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
                                val userType: String? = data?.user?.userType
                                // Inside the performLogin method
                                if (!accessToken.isNullOrBlank()) {
                                    // Save the access token
                                    tokenManager.saveToken(accessToken)
                                    Log.d("LoginViewModel", "User Type saved: $userType")

                                    // Log the saved token
                                    Log.d("TokenManager", "Access token saved: $accessToken")

                                    // Save user type
                                    val userType: String? = data?.user?.userType
                                    if (!userType.isNullOrBlank()) {
                                        tokenManager.saveUserType(userType)
                                    }

                                    // Invoke the callback with success
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
//                        onLoginResult(false, "Server error: ${response.code()}")
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
                }
            })
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