package com.example.graduationproject.ui.mainActivity.fragment.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.order.accORrej.AcceptOrRejectOrderResponse
import com.example.graduationproject.api.notifications.DataItem
import com.example.graduationproject.api.notifications.SellerNotificationResponse
import com.example.graduationproject.ui.login.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _notifications = MutableLiveData<List<DataItem?>>()
    val notifications: LiveData<List<DataItem?>> = _notifications

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchNotifications(accessToken: String) {
        ApiManager.getApisToken(accessToken).getSellerNotification(accessToken)
            .enqueue(object : Callback<SellerNotificationResponse> {
                override fun onResponse(
                    call: Call<SellerNotificationResponse>,
                    response: Response<SellerNotificationResponse>
                ) {
                    if (response.isSuccessful) {
                        _notifications.value = response.body()?.data ?: emptyList()

                        val orderNotifiId = response.body()?.data?.firstOrNull()?.id ?: 0
//                        tokenManager.saveOrderNotifiId(orderNotifiId)

                    } else {
                        _errorMessage.value = "Failed to load notifications"
                    }
                }

                override fun onFailure(call: Call<SellerNotificationResponse>, t: Throwable) {
                    _errorMessage.value = "Failed to load notifications: ${t.message}"
                }
            })
    }

    fun acceptOrRejectOrder(orderId: String, condition: String, callback: NotificationActionCallback) {
        val accessToken = tokenManager.getToken() ?: return
        ApiManager.getApisToken(accessToken)
            .acceptOrRejectOrder(accessToken, orderId, condition)
            .enqueue(object : Callback<AcceptOrRejectOrderResponse> {
                override fun onResponse(
                    call: Call<AcceptOrRejectOrderResponse>,
                    response: Response<AcceptOrRejectOrderResponse>
                ) {
                    when (response.code()) {
                        200 -> callback.onActionSuccess("${response.body()?.message}")
                        409 -> {
                            if (condition == "Accept") {
                                callback.onActionSuccess("${response.body()?.message} You have already ordered this product.")
                            } else if (condition == "Reject") {
                                callback.onActionSuccess("${response.body()?.message} Thank you for responding")
                            }
                        }
                        else -> callback.onActionFailure("Failed to $condition order")
                    }
                    Log.d("Notification", "${response.body()?.status}")
                    Log.d("Notification", "${response.code()}")


                    fetchNotifications(accessToken) // Refresh notifications
                }

                override fun onFailure(call: Call<AcceptOrRejectOrderResponse>, t: Throwable) {
                    callback.onActionFailure("Failed to $condition order: ${t.message}")
                }
            })
    }
}
