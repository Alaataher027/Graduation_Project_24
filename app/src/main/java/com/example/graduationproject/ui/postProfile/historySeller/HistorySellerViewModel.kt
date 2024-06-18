package com.example.graduationproject.ui.postProfile.historySeller

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.history.DataItemS
import com.example.graduationproject.api.model.history.HistorySellerResponse
import com.example.graduationproject.api.model.post.Data
import com.example.graduationproject.api.model.post.GetPostByIdResponse
import com.example.graduationproject.api.model.profile.ProfileResponse
import com.example.graduationproject.api.model.profile.Data as ProfileData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorySellerViewModel : ViewModel() {

    val postLiveData = MutableLiveData<List<Data>>()
    val profileLiveData = MutableLiveData<List<ProfileData>>()
    val errorLiveData = MutableLiveData<String>()

    fun getSellerHistory(accessToken: String, userID: Int) {
        ApiManager.getApisToken(accessToken)
            .getSellerHistory(accessToken, userID)
            .enqueue(object : Callback<HistorySellerResponse> {
                override fun onResponse(
                    call: Call<HistorySellerResponse>,
                    response: Response<HistorySellerResponse>
                ) {
                    if (response.isSuccessful) {
                        val histories = response.body()?.data ?: emptyList()
                        // Log the data
                        Log.d("HistorySellerViewModel", "Histories: $histories")
                        fetchPostData(accessToken, histories)
                        fetchUserProfiles(accessToken, histories)
                    } else {
                        errorLiveData.postValue("Failed to load seller history: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<HistorySellerResponse>, t: Throwable) {
                    errorLiveData.postValue("Failed to load seller history: ${t.message}")
                }
            })
    }

    private fun fetchPostData(accessToken: String, histories: List<DataItemS?>) {
        val posts = mutableListOf<Data>()
        var remainingHistories = histories.size

        for (history in histories) {
            if (history != null) {
                ApiManager.getApisToken(accessToken)
                    .getPostById(accessToken, (history.postId!!))
                    .enqueue(object : Callback<GetPostByIdResponse> {
                        override fun onResponse(
                            call: Call<GetPostByIdResponse>,
                            response: Response<GetPostByIdResponse>
                        ) {
                            if (response.isSuccessful) {
                                val post = response.body()?.data
                                if (post != null) {
                                    posts.add(post)
                                }
                            }
                            if (--remainingHistories == 0) {
                                postLiveData.postValue(posts)
                            }
                        }

                        override fun onFailure(call: Call<GetPostByIdResponse>, t: Throwable) {
                            if (--remainingHistories == 0) {
                                postLiveData.postValue(posts)
                            }
                        }
                    })
            } else {
                if (--remainingHistories == 0) {
                    postLiveData.postValue(posts)
                }
            }
        }
    }

    private fun fetchUserProfiles(accessToken: String, histories: List<DataItemS?>) {
        val profiles = mutableListOf<ProfileData>()
        var remainingHistories = histories.size

        for (history in histories) {
            if (history != null) {
                ApiManager.getApisToken(accessToken)
                    .getUserProfile(accessToken, (history.sellerId?.toIntOrNull() ?: 0))
                    .enqueue(object : Callback<ProfileResponse> {
                        override fun onResponse(
                            call: Call<ProfileResponse>,
                            response: Response<ProfileResponse>
                        ) {
                            if (response.isSuccessful) {
                                val profile = response.body()?.data
                                if (profile != null) {
                                    profiles.add(profile)
                                }
                            }
                            if (--remainingHistories == 0) {
                                profileLiveData.postValue(profiles)
                            }
                        }

                        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                            if (--remainingHistories == 0) {
                                profileLiveData.postValue(profiles)
                            }
                        }
                    })
            } else {
                if (--remainingHistories == 0) {
                    profileLiveData.postValue(profiles)
                }
            }
        }
    }
}
