package com.example.graduationproject.ui.mainActivity.fragment.notification.sellerNotification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.graduationproject.ui.login.TokenManager

class NotificationsViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsSellerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsSellerViewModel(tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
