package com.example.graduationproject.ui.mainActivity.fragment.createPost

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.WebServices
import com.example.graduationproject.api.model.post.createPost.ClassificationResponse
import com.example.graduationproject.api.model.post.createPost.PostResponse
import com.example.graduationproject.ui.login.TokenManager
import okhttp3.MediaType
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
        price: RequestBody,
        image: MultipartBody.Part,
        onResponse: (Boolean, String?) -> Unit
    ) {
        classifyImage(accessToken, image) { isSuccess, message, material ->
            if (isSuccess) {
                val materialBody = RequestBody.create(MediaType.parse("text/plain"), material)
                ApiManager.getApisToken(accessToken)
                    .createPost(accessToken, description, quantity, materialBody, price, image)
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
                                    val postId: Int? = postResponse?.data?.id
                                    tokenManager.saveUserPostId(userId ?: 0)
                                    onResponse(true, message)
                                    Log.d("CreatePostViewModel", "200, $userId")
                                } else {
                                    onResponse(false, message)
                                    Log.d("CreatePostViewModel", "else 200, $message")
                                }
                            } else {
                                onResponse(false, "Failed !")
                                Log.d("CreatePostViewModel", "else")
                            }
                        }

                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                            onResponse(false, "Network error: ${t.message}")
                            Log.d("CreatePost","${t.message}")
                        }
                    })
            } else {
                // Propagate the failure message
                onResponse(false, "Image classification failed: $message")
            }
        }
    }


    fun classifyImage(
        accessToken: String,
        image: MultipartBody.Part,
        onResponse: (Boolean, String?, String?) -> Unit
    ) {
        ApiManager.getApisToken(accessToken).classifyImage(accessToken, image)
            .enqueue(object : Callback<ClassificationResponse> {
                override fun onResponse(
                    call: Call<ClassificationResponse>,
                    response: Response<ClassificationResponse>
                ) {
                    val classificationResponse: ClassificationResponse? = response.body()
                    val status: Int? = classificationResponse?.status

                    if (response.isSuccessful) {
                        val classificationResponse: ClassificationResponse? = response.body()
                        val status: Int? = classificationResponse?.status
                        val message: String? = classificationResponse?.message
                        val data: String? = classificationResponse?.data

                        if (status == 200) {
                            // Successful response
                            onResponse(true, message, data)
                        } else {
                            // Unsuccessful response
                            onResponse(false, message, null)
                        }
                    } else {
                        // Unsuccessful response
                        onResponse(false, "Failed to classify image", null)
                        Log.d("CreatePostClassify", "Status code: ${response.code()}")
                        Log.d(
                            "CreatePostClassify",
                            "Error message: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ClassificationResponse>, t: Throwable) {
                    // Network error
                    onResponse(false, "Network error: ${t.message}", null)
                }
            })
    }

}
