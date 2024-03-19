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

class HomePostViewModel(private val tokenManager: TokenManager) : ViewModel() {


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
                        val homePostResponse: HomePostResponse? = response.body()
//                        // Check if data is not null and not empty
//                        val dataItems = homePostResponse?.data
//                        if (!dataItems.isNullOrEmpty()) {
//                            // Access userId from the first DataItem
//                            val userId: Int? = dataItems[0]?.userId
//                            tokenManager.saveUserPostId(userId?:0)
//
//                            Log.d("HomePostViewModel", "User Id: $userId")
//                        } else {
//                            Log.d("HomePostViewModel", "No data items received")
//                        }

                        _homePosts.value = response.body()?.data ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<HomePostResponse>, t: Throwable) {
                    Log.e("HomePostViewModel", "Failed to fetch home posts", t)

                }
            })
    }

}