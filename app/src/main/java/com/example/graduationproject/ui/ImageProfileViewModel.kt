package com.example.graduationproject.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.imageProfile.ImageProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageProfileViewModel : ViewModel() {
    fun uploadImage(
        accessToken: String,
        image: MultipartBody.Part,
        onResponse: (Boolean, String?) -> Unit
    ) {
        ApiManager.getApisToken(accessToken).imageProfile(accessToken, image)
            .enqueue(object : Callback<ImageProfileResponse> {
                override fun onResponse(
                    call: Call<ImageProfileResponse>,
                    response: Response<ImageProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message
                        if (status == 200) {
                            onResponse(true, message)
                            Log.d("ImageProfileViewModel", "200, $message")
                        } else {
                            onResponse(false, message)
                            Log.d("ImageProfileViewModel", "else 200, $message")
                        }
                    } else {
                        Log.d("ImageProfileViewModel", "else")
                        onResponse(false, "Failed to upload image")
                    }
                }
                override fun onFailure(call: Call<ImageProfileResponse>, t: Throwable) {
                    Log.d("ImageProfileViewModel", "onFailure")
                    onResponse(false, t.message)
                }
            })
    }
}