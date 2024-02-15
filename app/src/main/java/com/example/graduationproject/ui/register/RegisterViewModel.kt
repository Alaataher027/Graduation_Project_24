package com.example.graduationproject.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.register.RegisterResponse2
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    fun performRegister(
        name: String,
        email: String,
        password: String,
        password_confirmation: String,
        phone_number: String,
        user_type: String,
        lang: String,
        onRegisterResult: (Boolean, String) -> Unit
    ) {

        ApiManager.getApis()
            .userRegister(
                name, email, password, password_confirmation, phone_number, user_type, lang
            )
            .enqueue(object : Callback<RegisterResponse2> {
                override fun onResponse(
                    call: Call<RegisterResponse2>,
                    response: Response<RegisterResponse2>
                ) {
                    if (response.isSuccessful) {
                        val registerResponse: RegisterResponse2? = response.body()
                        if (registerResponse != null) {
                            // Successful registration
                            val status: Int? = registerResponse.status
                            val message: String? = registerResponse.message

                            if (status == 1) {
                                // Registration successful
                                onRegisterResult(true, message ?: "")
                            } else {
                                // Registration failed
                                onRegisterResult(false, message ?: "Registration failed")
                            }
                        } else {
                            // Handle null response body if needed
                            onRegisterResult(false, "Unexpected error occurred")
                        }
                    } else {
                        // Handle unsuccessful response
//                        onRegisterResult(false, "Server error: ${response.code()}") // ${response.message()
                        val json = response.errorBody()?.charStream()
                        Log.e("Tag", "$json")
                        val type = object : TypeToken<RegisterResponse2>() {}.type
                        onRegisterResult(
                            false,
                            "${Gson().fromJson<RegisterResponse2>(json, type).message}"
                        )
                    }
                }

                override fun onFailure(call: Call<RegisterResponse2>, t: Throwable) {
                    // Handle failure, for example, show an error message
                    onRegisterResult(false, "Network error: ${t.message}")
                }
            })
    }
}


//
//override fun onResponse(
//    call: Call<RegisterResponse>,
//    response: Response<RegisterResponse>
//) {
//    if (response.isSuccessful) {
//        onRegisterResult(true, "Registration successful")
//    } else {
//        val errorResponse = response.errorBody()?.string()
//        if (errorResponse != null) {
//            val errorResultObject =
//                Gson().fromJson(errorResponse, RegisterResponse::class.java)
//            val errorMessage =
//                "Registration failed: ${errorResultObject.message}\n" +
//                        "Name: ${errorResultObject.errors?.name}\n" +
//                        "Email: ${errorResultObject.errors?.email}\n" +
//                        "Password: ${errorResultObject.errors?.password}\n" +
//                        "Phone Number: ${errorResultObject.errors?.phoneNumber}"
//            onRegisterResult(false, errorMessage)
//        }
//    }
//}
//override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//    onRegisterResult(false, "Registration failed: ${t.message}")
//}
//}

//viewModelScope.launch {
//            try {
//                val registerResponse = ApiManager.getApis()
//                    .userRegister(name, email, password, password_confirmation, phone_number)
//                onRegisterResult(true, "Registration successful")
//
//            } catch (e: HttpException) {
//                val errorResponse = e.response()?.errorBody()?.string()
//                if (errorResponse != null) {
//                    val errorResultObject =
//                        Gson().fromJson(errorResponse, RegisterResponse::class.java)
//                    val errorMessage =
//                        "Registration failed: ${errorResultObject.message}\n" +
//                                "Name: ${errorResultObject.errors?.name}\n" +
//                                "Email: ${errorResultObject.errors?.email}\n" +
//                                "Password: ${errorResultObject.errors?.password}\n" +
//                                "Phone Number: ${errorResultObject.errors?.phoneNumber}"
//                    onRegisterResult(false, errorMessage)
//                }
//
//            }catch (e:Exception){
//                onRegisterResult(false, "Registration failed: ${e.message}")
//
//            }
//        }