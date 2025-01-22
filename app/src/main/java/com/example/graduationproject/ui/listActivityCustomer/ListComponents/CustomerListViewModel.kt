package com.example.graduationproject.ui.listActivityCustomer.ListComponents

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.api.model.profile.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerListViewModel : ViewModel() {

    fun viewData(
        accessToken: String,
        userId: Int, // Add userId parameter
        onDataLoaded: (Data?) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiManager.getApisToken(accessToken)
            .getUserProfile(accessToken,userId)
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {

                    Log.d("CustomerListViewMode", "first")

                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message
                        val profileResponse = response.body()
                        val data: Data? = profileResponse?.data
                        if (status == 200) {
                            onDataLoaded(data)
                        } else {
                            onError(message ?: "Unknown error")
                        }
                    } else {
                        // Log the email value in data
                        val errorMessage = "Error ${response.code()}: ${response.message()}"
                        onError(errorMessage)
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    onError(t.message ?: "Unknown error")
                }
            })
    }
}