package com.example.graduationproject.ui.mainActivity.fragment.createPost

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.post.createPost.ClassificationResponse
import com.example.graduationproject.api.model.post.createPost.PostResponse
import com.example.graduationproject.ui.login.TokenManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePostViewModel(private val tokenManager: TokenManager) : ViewModel() {

    val postCreationResult: MutableLiveData<Result<PostResponse>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun createPost(
        accessToken: String,
        description: RequestBody,
        quantity: RequestBody,
        price: RequestBody,
        material: RequestBody,
        image: MultipartBody.Part
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
                        val status: Int? = postResponse?.status
                        val message: String? = postResponse?.message

                        if (status == 200) {
                            val userId: Int? = postResponse?.data?.userId
                            val postId: Int? = postResponse?.data?.id
                            tokenManager.saveUserPostId(userId ?: 0)
                            postCreationResult.postValue(Result.success(postResponse))
                            Log.d("CreatePostViewModel", "Post created successfully, ID: $postId")
                        } else {
                            val errorMessage = "Failed to create post: $message"
                            error.postValue(errorMessage)
                            Log.d("CreatePostViewModel", errorMessage)
                        }
                    } else {
                        val errorMessage = "Failed to create post: ${response.message()}"
                        error.postValue(errorMessage)
                        Log.d("CreatePostViewModel", errorMessage)
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    val errorMessage = "Network error: ${t.message}"
                    error.postValue(errorMessage)
                    Log.d("CreatePostViewModel", errorMessage, t)
                }
            })
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

                    if (response.isSuccessful) {
                        val status: Int? = classificationResponse?.status
                        val message: String? = classificationResponse?.message
                        val data: String? = classificationResponse?.data

                        if (status == 200) {
                            onResponse(true, message, data)
                        } else {
                            onResponse(false, message, null)
                        }
                    } else {
                        onResponse(false, "Failed to classify image", null)
                        Log.d("CreatePostViewModel", "Failed to classify image: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ClassificationResponse>, t: Throwable) {
                    onResponse(false, "Network error: ${t.message}", null)
                    Log.e("CreatePostViewModel", "Failed to classify image", t)
                }
            })
    }
}