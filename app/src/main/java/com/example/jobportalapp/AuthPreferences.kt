package com.example.jobportalapp


import android.content.Context
import android.content.SharedPreferences

class AuthPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USER_LOGGED_IN = "user_logged_in"
        const val KEY_USER_ROLE = "user_role"
    }

    fun saveLoginStatus(isLoggedIn: Boolean, userRole: String) {
        sharedPreferences.edit().apply {
            putBoolean(KEY_USER_LOGGED_IN, isLoggedIn)
            putString(KEY_USER_ROLE, userRole)
            apply()
        }
    }

    fun getLoginStatus(): Pair<Boolean, String?> {
        val isLoggedIn = sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false)
        val userRole = sharedPreferences.getString(KEY_USER_ROLE, null)
        return Pair(isLoggedIn, userRole)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}