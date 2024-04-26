package com.example.graduationproject.ui.mainActivity.fragment.notification

import com.example.graduationproject.ui.login.TokenManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        TokenManager(applicationContext).saveFCMToken(token)

    }

//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//        // Respond to received messages
//    }
}
