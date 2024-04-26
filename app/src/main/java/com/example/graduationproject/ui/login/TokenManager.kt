package com.example.graduationproject.ui.login

import android.content.Context
import android.content.SharedPreferences

class TokenManager(val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    fun savePostId(id: Int) {
        sharedPreferences.edit().putInt("post_id", id).apply()
    }

    fun getPostId(): Int {
        return sharedPreferences.getInt("post_id", 0)
    }

    fun saveUserType(userType: String) {
        sharedPreferences.edit().putString("user_type", userType).apply()
    }

    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt("id", userId).apply()
    }

    fun saveUserPostId(userId: Int) {
        sharedPreferences.edit().putInt("user_id_post", userId).apply()
    }

    fun getUserPostId(): Int {
        return sharedPreferences.getInt("user_id_post", 0)
    }

    fun getUserType(): String? {
        return sharedPreferences.getString("user_type", null)
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt("id", 0)
    }

    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("access_token").apply()
    }

    fun clearUserId() {
        sharedPreferences.edit().remove("user_id").apply()
    }

    fun clearUserPostId() {
        sharedPreferences.edit().remove("user_id_post").apply()
    }

    fun saveFCMToken(token: String) {
        sharedPreferences.edit().putString("fcm_token", token).apply()
    }

    fun getFCMToken(): String? {
        return sharedPreferences.getString("fcm_token", null)
    }

}
