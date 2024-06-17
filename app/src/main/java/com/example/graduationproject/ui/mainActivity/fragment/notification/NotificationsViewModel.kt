package com.example.graduationproject.ui.mainActivity.fragment.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.notifications.accANDrej.DataItem
import com.example.graduationproject.api.model.order.accORrej.AcceptOrRejectOrderResponse
import com.example.graduationproject.api.model.notifications.accANDrej.SellerNotificationResponse
import com.example.graduationproject.api.model.notifications.yesAndNo.ConfirmNotificationSellerResponse
import com.example.graduationproject.api.model.notifications.yesAndNo.DataItemS
import com.example.graduationproject.api.model.order.yesORno.ConfirmNotificationResponse
import com.example.graduationproject.ui.login.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _notifications = MutableLiveData<List<Any?>>()
    val notifications: LiveData<List<Any?>> = _notifications

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val waitingNotifications = mutableListOf<DataItem>()
    private val confirmNotifications = mutableListOf<DataItemS>()

    fun fetchNotifications(accessToken: String) {
        fetchAcceptOrRejectNotifications(accessToken)
        fetchYesOrNoNotifications(accessToken)
    }

    private fun fetchAcceptOrRejectNotifications(accessToken: String) {
        ApiManager.getApisToken(accessToken).getWaitingNotification(accessToken)
            .enqueue(object : Callback<SellerNotificationResponse> {
                override fun onResponse(
                    call: Call<SellerNotificationResponse>,
                    response: Response<SellerNotificationResponse>
                ) {
                    if (response.isSuccessful) {
                        waitingNotifications.clear()
                        response.body()?.data?.let {
                            waitingNotifications.addAll(it.filterNotNull())
                        }
                        mergeAndSortNotifications()
                    } else {
                        _errorMessage.value = "Failed to load notifications"
                    }
                }

                override fun onFailure(call: Call<SellerNotificationResponse>, t: Throwable) {
                    _errorMessage.value = "Failed to load notifications: ${t.message}"
                }
            })
    }

    private fun fetchYesOrNoNotifications(accessToken: String) {
        ApiManager.getApisToken(accessToken).getConfirmNotificationSeller(accessToken)
            .enqueue(object : Callback<ConfirmNotificationSellerResponse> {
                override fun onResponse(
                    call: Call<ConfirmNotificationSellerResponse>,
                    response: Response<ConfirmNotificationSellerResponse>
                ) {
                    if (response.isSuccessful) {
                        confirmNotifications.clear()
                        response.body()?.data?.let {
                            confirmNotifications.addAll(it.filterNotNull())
                        }
                        mergeAndSortNotifications()
                    } else {
                        _errorMessage.value = "Failed to load notifications"
                    }
                }

                override fun onFailure(
                    call: Call<ConfirmNotificationSellerResponse>,
                    t: Throwable
                ) {
                    _errorMessage.value = "Failed to load notifications: ${t.message}"
                }
            })
    }

    private fun mergeAndSortNotifications() {
        val allNotifications = mutableListOf<Any?>()
        allNotifications.addAll(waitingNotifications)
        allNotifications.addAll(confirmNotifications)

        allNotifications.sortByDescending { notification ->
            when (notification) {
                is DataItem -> notification.createdAt
                is DataItemS -> notification.createdAt
                else -> null
            }
        }

        _notifications.value = allNotifications
    }

    fun acceptOrRejectOrder(
        orderId: String,
        condition: String,
        callback: NotificationActionCallback
    ) {
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

    fun yesOrNoOrder(orderId: String, condition: String, callback: NotificationActionCallback) {
        val accessToken = tokenManager.getToken() ?: return
        ApiManager.getApisToken(accessToken)
            .yesOrNoConfirm(accessToken, orderId, condition)
            .enqueue(object : Callback<ConfirmNotificationResponse> {
                override fun onResponse(
                    call: Call<ConfirmNotificationResponse>,
                    response: Response<ConfirmNotificationResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.onActionSuccess("${response.body()?.message}")
                    } else {
                        callback.onActionFailure("Failed to $condition order")
                    }
                }

                override fun onFailure(call: Call<ConfirmNotificationResponse>, t: Throwable) {
                    callback.onActionFailure("Failed to $condition order: ${t.message}")
                }
            })
    }


}
