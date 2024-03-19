package com.example.graduationproject.ui.mainActivity.fragment.createPost

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.post.createPost.PostResponse
import com.example.graduationproject.ui.login.TokenManager
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody

class CreatePostViewModel(private val tokenManager: TokenManager) : ViewModel() {

    fun createPost(
        accessToken: String,
        description: RequestBody,
        quantity: RequestBody,
        material: RequestBody,
        price: RequestBody,
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
                        val postResponse: PostResponse? = response.body()
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message
                        if (status == 200) {
                            val userId: Int? = postResponse?.data?.userId
                            tokenManager.saveUserPostId(userId?:0)
                            onResponse(true, message)
                            Log.d("CreatePostViewModel", "200, $userId")
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
