package com.example.graduationproject.ui.checkOTP

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.CheckCodeResponse
import com.example.graduationproject.api.model.login.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpVertificationViewModel : ViewModel() {

    fun checkCode(
        code: String,
        lang: String,
        onCodeChecked: (Boolean, String) -> Unit
    ) {
        ApiManager.getApis()
            .checkCode(code, lang)  // Assuming checkPassword is the correct API method
            .enqueue(object : Callback<CheckCodeResponse> {
                override fun onResponse(
                    call: Call<CheckCodeResponse>,
                    response: Response<CheckCodeResponse>
                ) {
                    if (response.isSuccessful) {
                        val checkPasswordResponse = response.body()
                        if (checkPasswordResponse != null) {
                            // Handle successful response
                            onCodeChecked(true, checkPasswordResponse.message ?: "")
                        } else {
                            // Handle null response body
                            onCodeChecked(false, "Null response body")
                        }
                    } else {
                        // Handle unsuccessful response (e.g., HTTP error code)
//                        onCodeChecked(false, "Error: ${response.code()}")
                        val json = response.errorBody()?.charStream()
                        Log.e("Tag", "$json")
                        val type = object : TypeToken<CheckCodeResponse>() {}.type
                        onCodeChecked(
                            false,
                            "${Gson().fromJson<CheckCodeResponse>(json, type).message}"
                        )
                    }
                }

                override fun onFailure(call: Call<CheckCodeResponse>, t: Throwable) {
                    // Handle network or unexpected errors
                    onCodeChecked(false, "Failed to send request: ${t.message}")
                }
            })
    }
}
