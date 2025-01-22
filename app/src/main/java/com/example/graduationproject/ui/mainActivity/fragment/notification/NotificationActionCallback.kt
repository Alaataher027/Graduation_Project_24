package com.example.graduationproject.ui.mainActivity.fragment.notification

interface NotificationActionCallback {
    fun onActionSuccess(message: String)
    fun onActionFailure(message: String)
}
