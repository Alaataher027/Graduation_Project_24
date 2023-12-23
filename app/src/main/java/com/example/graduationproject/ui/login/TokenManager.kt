package com.example.graduationproject.ui.login

import android.content.Context
import android.content.SharedPreferences

class TokenManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }
}
