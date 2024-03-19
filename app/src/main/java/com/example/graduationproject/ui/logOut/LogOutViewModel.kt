package com.example.graduationproject.ui.logOut

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.logout.LogOutResponse
import com.example.graduationproject.ui.login.TokenManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogOutViewModel(private val tokenManager: TokenManager) : ViewModel() {

    fun performLogOut(accessToken: String, onLogOutResult: (Boolean, String) -> Unit) {
        Log.i("LogOutViewModel", "Starting logout process with access token: $accessToken")
        ApiManager.getApisToken(accessToken)
            .logOut(accessToken) // Pass the access token to the logout function
            .enqueue(object : Callback<LogOutResponse> {
                override fun onResponse(
                    call: Call<LogOutResponse>,
                    response: Response<LogOutResponse>
                ) {
                    Log.i("LogOutViewModel", "Logout response received $accessToken")
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        Log.i("LogOutViewModel", "Logout response successful $accessToken")
                        if (status == 200) {
                            // Logout successful
                            Log.i("LogOutViewModel", "Logout successful: $accessToken")
                            tokenManager.clearToken()
                            tokenManager.clearUserId()
                            onLogOutResult(true, message ?: "Logout successful")
                        } else {
                            Log.i("LogOutViewModel", "Logout failed: $accessToken")
                            onLogOutResult(false, message ?: "Logout failed")
                        }
                    } else {
                        // Handle non-successful response
                        Log.i("LogOutViewModel", "Logout response unsuccessful: ${response.code()} , token : $accessToken")

                        onLogOutResult(false, "Error: ${response.code()}")

                    }
                }
                override fun onFailure(call: Call<LogOutResponse>, t: Throwable) {
                    // Handle network errors
                    Log.e("LogOutViewModel", "Network error: ${t.message}", t)
                    onLogOutResult(false, "Network error: ${t.message}")
                }
            })
    }
}
