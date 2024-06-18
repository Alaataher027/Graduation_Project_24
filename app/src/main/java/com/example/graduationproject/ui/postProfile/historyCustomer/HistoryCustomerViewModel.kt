package com.example.graduationproject.ui.postProfile.historyCustomer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.history.DataItemC
import com.example.graduationproject.api.model.history.HistoryCustomerResponse
import com.example.graduationproject.api.model.post.Data
import com.example.graduationproject.api.model.post.GetPostByIdResponse
import com.example.graduationproject.api.model.profile.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryCustomerViewModel : ViewModel() {

    val postLiveData = MutableLiveData<List<Data>>()
    val profileLiveData = MutableLiveData<List<com.example.graduationproject.api.model.profile.Data>>()
    val errorLiveData = MutableLiveData<String>()

    fun getCustomerHistory(accessToken: String, userID: Int) {
        ApiManager.getApisToken(accessToken)
            .getCustomerHistory(accessToken, userID)// buyerId
            .enqueue(object : Callback<HistoryCustomerResponse> {
                override fun onResponse(
                    call: Call<HistoryCustomerResponse>,
                    response: Response<HistoryCustomerResponse>
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

                override fun onFailure(call: Call<HistoryCustomerResponse>, t: Throwable) {
                    errorLiveData.postValue("Failed to load seller history: ${t.message}")
                }
            })
    }

    private fun fetchPostData(accessToken: String, histories: List<DataItemC?>) {
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

    private fun fetchUserProfiles(accessToken: String, histories: List<DataItemC?>) {
        val profiles = mutableListOf<com.example.graduationproject.api.model.profile.Data>()
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