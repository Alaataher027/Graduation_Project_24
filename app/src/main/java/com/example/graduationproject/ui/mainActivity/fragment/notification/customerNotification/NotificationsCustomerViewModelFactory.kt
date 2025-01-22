package com.example.graduationproject.ui.mainActivity.fragment.notification.customerNotification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduationproject.ui.login.TokenManager

class NotificationsCustomerViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsCustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsCustomerViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
