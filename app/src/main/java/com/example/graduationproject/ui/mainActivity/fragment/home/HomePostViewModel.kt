package com.example.graduationproject.ui.mainActivity.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.post.createPost.PostResponse
import com.example.graduationproject.api.model.post.deletePost.DeletePostResponse
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.api.model.post.postHome.HomePostResponse
import com.example.graduationproject.ui.login.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePostViewModel() : ViewModel() {


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
                        val homePosts = response.body()?.data ?: emptyList()
                        _homePosts.value = homePosts
                        Log.d("HomePosts", "${response.body()}")

                        // Print updated_at for each DataItem
                        homePosts.forEach { dataItem ->
                            Log.d("HomePosts", "updated_at: ${dataItem?.updatedAt}")
                        }
                    }
                }

                override fun onFailure(call: Call<HomePostResponse>, t: Throwable) {
                    Log.e("HomePostViewModel", "Failed to fetch home posts", t)
                }
            })
    }



}