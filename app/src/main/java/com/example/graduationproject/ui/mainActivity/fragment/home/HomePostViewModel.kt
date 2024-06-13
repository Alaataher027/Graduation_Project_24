package com.example.graduationproject.ui.mainActivity.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.order.sendOrder.OrderResponse
import com.example.graduationproject.api.model.post.deletePost.DeletePostResponse
import com.example.graduationproject.api.model.post.editPost.EditPostResponse
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.api.model.post.postHome.HomePostResponse
import com.example.graduationproject.api.model.post.savePost.SavePostResponse
import com.example.graduationproject.ui.login.TokenManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePostViewModel(private val tokenManager: TokenManager) : ViewModel() {

    // الدالة لتصنيف المنشورات حسب نوع المادة
    fun filterPostsByMaterial(posts: List<DataItem?>, materials: List<String>): List<DataItem?> {
        return posts.filter { post ->
            val material = post?.material
            materials.any { it == material }
        }
    }

    private val _homePosts = MutableLiveData<List<DataItem?>>()
    val homePosts: LiveData<List<DataItem?>> = _homePosts

    fun fetchHomePosts(accessToken: String) {
        ApiManager.getApisToken(accessToken).getHomePosts(accessToken)
            .enqueue(object : Callback<HomePostResponse> {
                override fun onResponse(
                    call: Call<HomePostResponse>,
                    response: Response<HomePostResponse>
                ) {
                    if (response.isSuccessful) {

                        _homePosts.value = response.body()?.data ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<HomePostResponse>, t: Throwable) {
                    Log.e("HomePostViewModel", "Failed to fetch home posts", t)

                }
            })
    }

    fun savePost(accessToken: String, postId: String) {
        ApiManager.getApisToken(accessToken).savePost(accessToken, postId)
            .enqueue(object : Callback<SavePostResponse> {
                override fun onResponse(
                    call: Call<SavePostResponse>,
                    response: Response<SavePostResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message
                        if (status == 200) {
                            Log.d("HomePostViewModel", "200, $message")
                        } else {
                            Log.d("HomePostViewModel", "else 200, $message")
                        }
                    } else {
                        Log.d("HomePostViewModel", "else")
                    }
                }

                override fun onFailure(call: Call<SavePostResponse>, t: Throwable) {
                    Log.e("HomePostViewModel", "Failed to save post", t)
                }
            })
    }

    fun deletePost(accessToken: String, id: Int) {
        ApiManager.getApisToken(accessToken).deletePost(accessToken, id)
            .enqueue(object : Callback<DeletePostResponse> {
                override fun onResponse(
                    call: Call<DeletePostResponse>,
                    response: Response<DeletePostResponse>
                ) {
                    if (response.isSuccessful) {

                        val updatedList = mutableListOf<DataItem?>()
                        _homePosts.value?.forEach { post ->
                            if (post?.id != id) {
                                updatedList.add(post)
                            }
                        }
                        _homePosts.value = updatedList.toList()

                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message
                        if (status == 200) {
                            Log.d("HomePostViewModel", "Post deleted successfully")

                            fetchHomePosts(accessToken)
                        } else {
                            Log.d("HomePostViewModel", "Failed to delete post: $message")
                        }
                    }
                }

                override fun onFailure(call: Call<DeletePostResponse>, t: Throwable) {
                    Log.e("HomePostViewModel", "Failed to delete post", t)
                }
            })
    }

    fun editPost(
        accessToken: String,
        id: Int,
        description: RequestBody,
        quantity: RequestBody,
        material: RequestBody,
        price: RequestBody,
        image: MultipartBody.Part,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {

        ApiManager.getApisToken(accessToken)
            .editPost(accessToken, id, description, quantity, material, price, image)
            .enqueue(object : Callback<EditPostResponse> {
                override fun onResponse(
                    call: Call<EditPostResponse>,
                    response: Response<EditPostResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message
                        if (status == 200) {
                            onSuccess.invoke(message ?: "")
                            Log.d("HomePostViewModel", "Post edit successfully")
                        } else {
                            onError.invoke(message ?: "Unknown error")
                            Log.d("HomePostViewModel", "Failed to edit post: $message")
                        }
                    } else {
                        Log.d("HomePostViewModel", "Failed to edit post:***${id}")

                        onError.invoke("Error ${response.code()}: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EditPostResponse>, t: Throwable) {
                    Log.e("HomePostViewModel", "Failed to edit post", t)
                }
            })

    }


    fun orderPost(
        accessToken: String,
        postId: String,
        buyerId: String,
        callback: (String) -> Unit
    ) {
        ApiManager.getApisToken(accessToken).orderAndAddToCart(accessToken, postId, buyerId)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    if (response.isSuccessful) {
                        // Log the response body
                        Log.d("HomePostViewModel", "Response body: ${response.body()}")

                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message
                        val orderID: Int? = response.body()?.data?.id
                        if (status == 200) {
//                            if (orderID != null) {
//                                tokenManager.saveOrderId(orderID)
//                            }
                            Log.d("HomePostViewModel", "200, $message")
                            callback("$message")
                        }
                        else if (response.code() == 409){
                            Log.d("HomePostViewModel", "409, $message")
                            callback("The product is not available.")
                        }
                        else {
                            Log.d("HomePostViewModel", "else 200, $message")
                            callback("$message")
                        }
                    }
                    else {
                        // Handle different status codes including 409
                        val status: Int = response.code()
                        val message: String? = response.body()?.message ?: response.message()
                        Log.d("HomePostViewModel", "****$status, $message")
                        callback(message ?: "Failed to place order")
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Log.e("HomePostViewModel", "Failed to order", t)
                    callback("Failed to place order: ${t.message}")
                }
            })
    }



}