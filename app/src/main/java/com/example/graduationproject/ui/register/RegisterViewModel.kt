package com.example.graduationproject.ui.register

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.WebServices
import com.example.graduationproject.api.model.RegisterResponse
import com.example.graduationproject.data.User
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    fun performRegister(
        userName: String,
        email: String,
        password: String,
        phoneNumber: String,
        password_confirmation: String,
        onRegisterResult: (Boolean, String) -> Unit
    ) {

        ApiManager.getApis()
            .userRegister(userName, email, password, phoneNumber, password_confirmation)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        onRegisterResult(true, "Registration successful")
                    } else {
                        val errorResponse = response.errorBody()?.string()
                        if (errorResponse != null) {
                            val errorResultObject =
                                Gson().fromJson(errorResponse, RegisterResponse::class.java)
                            val errorMessage =
                                "Registration failed: ${errorResultObject.message}\n" +
                                        "Name: ${errorResultObject.errors?.name}\n" +
                                        "Email: ${errorResultObject.errors?.email}\n" +
                                        "Password: ${errorResultObject.errors?.password}\n" +
                                        "Phone Number: ${errorResultObject.errors?.phoneNumber}"
                            onRegisterResult(false, errorMessage)
                        }
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    onRegisterResult(false, "Registration failed: ${t.message}")
                }
            })
    }
}