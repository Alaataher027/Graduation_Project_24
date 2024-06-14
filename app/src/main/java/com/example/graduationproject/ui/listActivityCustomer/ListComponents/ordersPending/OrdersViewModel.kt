package com.example.graduationproject.ui.listActivityCustomer.ListComponents.ordersPending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.DataItem
import com.example.graduationproject.api.model.Order2Response
import com.example.graduationproject.api.model.post.GetPostByIdResponse
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.api.model.profile.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel() {

    val profilesLiveData = MutableLiveData<List<Data>>()
    val postLiveData = MutableLiveData<List<com.example.graduationproject.api.model.post.Data>>()
    val errorLiveData = MutableLiveData<String>()

    fun getOrders(accessToken: String, buyerId: Int) {
        ApiManager.getApisToken(accessToken)
            .getOrdersForUser(accessToken, buyerId)
            .enqueue(object : Callback<Order2Response> {
                override fun onResponse(
                    call: Call<Order2Response>,
                    response: Response<Order2Response>
                ) {
                    if (response.isSuccessful) {
                        val orders = response.body()?.data ?: emptyList()
                        fetchUserProfiles(accessToken, orders)
                        fetchPostData(accessToken, orders)
                    } else {
                        errorLiveData.postValue("Failed to load orders: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Order2Response>, t: Throwable) {
                    errorLiveData.postValue("Failed to load orders: ${t.message}")
                }
            })
    }

    private fun fetchUserProfiles(accessToken: String, orders: List<DataItem?>) {
        val profiles = mutableListOf<Data>()
        var remainingOrders = orders.size

        for (order in orders) {
            if (order != null) {
                ApiManager.getApisToken(accessToken)
                    .getUserProfile(accessToken, order.sellerId?.toInt() ?: 0)
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
                            if (--remainingOrders == 0) {
                                profilesLiveData.postValue(profiles)
                            }
                        }

                        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                            if (--remainingOrders == 0) {
                                profilesLiveData.postValue(profiles)
                            }
                        }
                    })
            } else {
                if (--remainingOrders == 0) {
                    profilesLiveData.postValue(profiles)
                }
            }
        }
    }


    fun fetchPostData(accessToken: String, orders: List<DataItem?>){
        val posts = mutableListOf<com.example.graduationproject.api.model.post.Data>()
        var remainingOrders = orders.size

        for (order in orders) {
            if (order != null) {
                ApiManager.getApisToken(accessToken)
                    .getPostById(accessToken, order.postId ?: 0)
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
                            if (--remainingOrders == 0) {
                                postLiveData.postValue(posts)
                            }
                        }

                        override fun onFailure(call: Call<GetPostByIdResponse>, t: Throwable) {
                            if (--remainingOrders == 0) {
                                postLiveData.postValue(posts)
                            }
                        }
                    })
            } else {
                if (--remainingOrders == 0) {
                    postLiveData.postValue(posts)
                }
            }
        }

    }
}



//    fun getOrders(accessToken: String, buyerId: Int) {
//        ApiManager.getApisToken(accessToken)
//            .getOrdersForUser(accessToken, buyerId)
//            .enqueue(object : Callback<Order2Response> {
//                override fun onResponse(
//                    call: Call<Order2Response>,
//                    response: Response<Order2Response>
//                ) {
//                    if (response.isSuccessful) {
//                        val orders = response.body()?.data as List<DataItem>?
//                        _orders.value = orders!!
//
//                        // Fetch seller data for each order
//                        orders?.forEach { order ->
//                            order.sellerId?.let { sellerId ->
//                                getUserData(accessToken, sellerId)
//                            }
//                        }
//                    } else {
//                        _error.value = "Error: ${response.message()}"
//                    }
//                }
//
//                override fun onFailure(call: Call<Order2Response>, t: Throwable) {
//                    _error.value = "Error: ${t.message}"
//                }
//            })
//    }
//
//    private fun getUserData(accessToken: String, userId: String) {
//        ApiManager.getApisToken(accessToken)
//            .getUserProfile(accessToken, userId)
//            .enqueue(object : Callback<ProfileResponse> {
//                override fun onResponse(
//                    call: Call<ProfileResponse>,
//                    response: Response<ProfileResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val data = response.body()?.data
//                        val updatedMap = _sellerData.value.orEmpty().toMutableMap()
//                        if (data != null) {
//                            updatedMap[userId] = data
//                            _sellerData.value = updatedMap
//                        }
//                    } else {
//                        // Handle error
//                    }
//                }
//
//                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
//                    // Handle failure
//                }
//            })
//    }
