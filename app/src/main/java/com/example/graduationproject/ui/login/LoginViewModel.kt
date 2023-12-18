package com.example.graduationproject.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.login.LoginResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    fun performLogin(
        email: String,
        password: String,
        onLoginResult: (Boolean, String) -> Unit
    ) {
        ApiManager.getApis()
            .userLogin(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    Log.d("LoginViewModel", "Response: ${response.code()}, ${response.body()}")
                    if (response.isSuccessful) {
                        onLoginResult(true, "Login successful")
                    } else {
                        val errorResponse = response.errorBody()?.string()
                        if (errorResponse != null) {
                            val errorResultObject =
                                Gson().fromJson(errorResponse, LoginResponse::class.java)
                            val errorMessage =
                                "Login failed: ${errorResultObject.message}\n" +
                                        "Email: ${errorResultObject.loginErrors?.email}\n" +
                                        "Password: ${errorResultObject.loginErrors?.password}\n"
                            Log.d("LoginViewModel", "Error Response: $errorResponse")
                            onLoginResult(false, errorMessage)

                        }
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onLoginResult(false, "Login failed: ${t.message}")
                }
            })
    }
}