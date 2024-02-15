package com.example.graduationproject.ui.mainActivityCustomer.ListComponents.logOut

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.logout.ErrorResponse
import com.example.graduationproject.api.model.logout.LogOutResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogOutViewModel : ViewModel() {
    fun performLogOut(accessToken: String, onLogOutResult: (Boolean, String) -> Unit) {
        ApiManager.getApis()
            .logOut(accessToken) // Pass the access token to the logout function
            .enqueue(object : Callback<LogOutResponse> {
                override fun onResponse(call: Call<LogOutResponse>, response: Response<LogOutResponse>) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        if (status == 200) {
                            // Logout successful
                            SessionManager.clearToken(ApplicationProvider.getApplicationContext())
                            onLogOutResult(true, message ?: "Logout successful")
                        } else {
                            // Logout failed
                            onLogOutResult(false, message ?: "Logout failed")
                        }
                    } else {
                        val json = response.errorBody()?.charStream()
                        Log.e("Tag", "$json")
                        val type = object : TypeToken<ErrorResponse>() {}.type
                        val errorMessage = Gson().fromJson<ErrorResponse>(json, type).message
                        onLogOutResult(false, errorMessage ?: "Unknown error")
                    }
                }

                override fun onFailure(call: Call<LogOutResponse>, t: Throwable) {
                    // Handle network errors
                    onLogOutResult(false, "Network error: ${t.message}")
                }
            })
    }
}
