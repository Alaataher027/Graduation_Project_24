package com.example.graduationproject.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.login.loginGoogle.LoginGoogleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleSignInViewModel(application: Application) : AndroidViewModel(application) {
    val tokenManager = TokenManager(application)

    fun signInWithGoogle(onLoginResult: (Boolean, String) -> Unit) {ApiManager.getApis().loginWithGoogle().enqueue(object : Callback<LoginGoogleResponse> {
        override fun onResponse(
            call: Call<LoginGoogleResponse>,
            response: Response<LoginGoogleResponse>
        ) {
            Log.d("ViewModelGoogle", "Response received: ${response.body()}")

            if (response.isSuccessful) {
                val loginResponse: LoginGoogleResponse? = response.body()
                if (loginResponse != null) {
                    val status: Int? = loginResponse.status
                    val message: String? = loginResponse.message
                    if (status == 200) {
                        val id: Int? = loginResponse.data?.id
//                            onLoginResult(true, message?:"")
                        // Inside the performLogin method

                        if (id != 0) {
                            // Save the access token
                            tokenManager.saveUserId(id ?: 0)

                            // Invoke the callback with success
                            onLoginResult(true, message ?: "")
                            Log.d("ViewModelGoogle", "${message}, ${id}")
                        } else {
                            Log.d("ViewModelGoogle", "${message}, ${id}")

                            onLoginResult(false, "Invalid access token")
                        }

                    } else {
                        Log.d("ViewModelGoogle", "${message}")

                        onLoginResult(false, message ?: "Login failed")
                    }
                } else {
                    Log.d("ViewModelGoogle", "Unexpected error occurred")

                    onLoginResult(false, "Unexpected error occurred")
                }
            }
        }

        override fun onFailure(call: Call<LoginGoogleResponse>, t: Throwable) {
            onLoginResult(false, "Network error: ${t.message}")
            Log.d("ViewModelGoogle", "Network error")

        }
    })


    }
}