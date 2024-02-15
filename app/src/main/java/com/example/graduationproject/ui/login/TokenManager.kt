package com.example.graduationproject.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenManager(val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE)


    fun saveToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    fun saveUserType(userType: String) {
        sharedPreferences.edit().putString("user_type", userType).apply()
    }

    fun getUserType(): String? {
        return sharedPreferences.getString("user_type", null)
    }

    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }
}
