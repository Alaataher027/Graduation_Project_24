package com.example.graduationproject.ui.mainActivity.fragment.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.post.PostResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreatePostViewModel : ViewModel() {

    fun createPost(
        accessToken: String,
        description: String,
        quantity: String,
        material: String,
        price: String,
        image: MultipartBody.Part,
        onResponse: (Boolean, String?) -> Unit
    ) {
        ApiManager.getApisToken(accessToken)
            .createPost(accessToken, description, quantity, material, price, image)
            .enqueue(object : Callback<PostResponse> {
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message
                        if (status == 200) {
                            onResponse(true, message)
                            Log.d("CreatePostViewModel", "200, $message")
                        } else {
                            onResponse(false, message)
                            Log.d("CreatePostViewModel", "else 200, $message")
                        }
                    } else {
                        Log.d("CreatePostViewModel", "else")
                        onResponse(false, "Failed !")
                    }
                }
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    onResponse(false, "Network error: ${t.message}")

                }
            })
    }
}