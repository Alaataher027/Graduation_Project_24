package com.example.graduationproject.ui.resetPass

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.ResetPasswordResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewPasswordViewModel : ViewModel() {

    fun resetPassword(
        email: String,
        password: String,
        passwordConfirmation: String,
        lang: String,
        onResetPass: (Boolean, String) -> Unit
    ) {
        ApiManager.getApis()
            .resetPassword(email, password, passwordConfirmation, lang)
            .enqueue(object : Callback<ResetPasswordResponse> {
                override fun onResponse(
                    call: Call<ResetPasswordResponse>,
                    response: Response<ResetPasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        val resetPasswordResponse = response.body()
                        if (resetPasswordResponse != null) {
                            // Handle successful response
                            onResetPass(true, resetPasswordResponse.message ?: "")
                        } else {
                            // Handle null response body
                            onResetPass(false, "Null response body")
                        }
                    } else {
                        // Handle unsuccessful response (e.g., HTTP error code)
//                        onResetPass(false, "Error: ${response.code()}")
                        val json = response.errorBody()?.charStream()
                        Log.e("Tag", "$json")
                        val type = object : TypeToken<ResetPasswordResponse>() {}.type
                        onResetPass(
                            false,
                            "${Gson().fromJson<ResetPasswordResponse>(json, type).message}"
                        )
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    // Handle network or unexpected errors
                    onResetPass(false, "Failed to send request: ${t.message}")
                }
            })
    }
}
