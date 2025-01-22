package com.example.graduationproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.post.Data
import com.example.graduationproject.api.model.post.GetPostByIdResponse
import com.example.graduationproject.api.model.post.savePost.DataItem
import com.example.graduationproject.api.model.post.savePost.SavePostsListResponse
import com.example.graduationproject.ui.login.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedPostsViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _savedPosts = MutableLiveData<List<DataItem?>>()
    val savedPosts: LiveData<List<DataItem?>> = _savedPosts

    private val _postData = MutableLiveData<Data?>()  // Change to Data?
    val postData: LiveData<Data?> = _postData

    fun fetchSavedPosts() {
        val accessToken = tokenManager.getToken() ?: ""
        ApiManager.getApisToken(accessToken).getSavedPosts(accessToken)
            .enqueue(object : Callback<SavePostsListResponse> {
                override fun onResponse(
                    call: Call<SavePostsListResponse>,
                    response: Response<SavePostsListResponse>
                ) {
                    if (response.isSuccessful) {
                        _savedPosts.value = response.body()?.data ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<SavePostsListResponse>, t: Throwable) {
                    // Handle failure
                }
            })
    }

    fun getPost(accessToken: String, postId: Int) {
        ApiManager.getApisToken(accessToken).getPostById(accessToken, postId)
            .enqueue(object : Callback<GetPostByIdResponse> {
                override fun onResponse(
                    call: Call<GetPostByIdResponse>,
                    response: Response<GetPostByIdResponse>
                ) {
                    if (response.isSuccessful) {
                        _postData.value = response.body()?.data
                    }
                }

                override fun onFailure(call: Call<GetPostByIdResponse>, t: Throwable) {
                    // Handle failure
                }
            })
    }
}
