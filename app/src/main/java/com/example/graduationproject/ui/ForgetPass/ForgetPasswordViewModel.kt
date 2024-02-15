package com.example.graduationproject.ui.ForgetPass

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.ForgetPasswordResponse
import com.example.graduationproject.api.model.login.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordViewModel : ViewModel() {

    fun sendCode(
        email: String,
        lang: String,
        onCodeSended: (Boolean, String) -> Unit

    ) {
        ApiManager.getApis()
            .forgetPassword(email, lang)
            .enqueue(object : Callback<ForgetPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgetPasswordResponse>,
                    response: Response<ForgetPasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        val forgetPasswordResponse = response.body()
                        if (forgetPasswordResponse != null) {
                            // Handle successful response
                            onCodeSended(true, forgetPasswordResponse.message ?: "")
                        } else {
                            // Handle null response body
                            onCodeSended(false, "Null response body")
                        }
                    } else {
                        // Handle unsuccessful response (e.g., HTTP error code)
//                        onCodeSended(false, "Error: ${response.message()}")
                        val json = response.errorBody()?.charStream()
                        Log.e("Tag", "$json")
                        val type = object : TypeToken<ForgetPasswordResponse>() {}.type
                        onCodeSended(
                            false,
                            "${Gson().fromJson<ForgetPasswordResponse>(json, type).message}"
                        )
                    }
                }

                override fun onFailure(call: Call<ForgetPasswordResponse>, t: Throwable) {
                    // Handle network or unexpected errors
                    onCodeSended(false, "Failed to send request: ${t.message}")
                }
            })

    }
}