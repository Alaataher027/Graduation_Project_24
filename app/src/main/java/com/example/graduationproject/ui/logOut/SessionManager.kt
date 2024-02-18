package com.example.graduationproject.ui.logOut

import android.content.Context
import android.util.Log

          /////////////// NOT Used /////////////////

object SessionManager {
    private const val PREF_NAME = "MyAppPreferences"
    private const val KEY_ACCESS_TOKEN = "accessToken"

    fun saveToken(context: Context, token: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ACCESS_TOKEN, token)
            .apply()
    }

    fun clearToken(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_ACCESS_TOKEN)
            .apply()
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ACCESS_TOKEN, null)
    }


}