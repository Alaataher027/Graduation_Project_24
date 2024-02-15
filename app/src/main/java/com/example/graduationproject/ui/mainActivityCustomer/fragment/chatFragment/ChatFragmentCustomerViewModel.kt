package com.example.graduationproject.ui.mainActivityCustomer.fragment.chatFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.login.LoginResponse2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatFragmentCustomerViewModel : ViewModel() {

//    private val _loginResponse = MutableLiveData<LoginResponse2>()
//    val loginResponse: LiveData<LoginResponse2> get() = _loginResponse
//
//    fun login(email: String, password: String, language: String) {
//        ApiManager.getApis().userLogin(email, password, language)
//            .enqueue(object : Callback<LoginResponse2> {
//                override fun onResponse(call: Call<LoginResponse2>, response: Response<LoginResponse2>) {
//                    if (response.isSuccessful) {
//                        _loginResponse.value = response.body()
//                    } else {
//                        // Handle unsuccessful response
//                    }
//                }
//
//                override fun onFailure(call: Call<LoginResponse2>, t: Throwable) {
//                    // Handle network failure
//                }
//            })
//    }
}
