package com.dedsec.freshwalls.utils

import android.content.Context
import com.google.gson.Gson

class SharedPreferenceManager(context: Context) {
    companion object {
        const val KEY_USER_DESCRIPTION = "user_description"
        const val KEY_NOTIFICATION_ENABLE = "notification"
        const val KEY_CURRENT_WALLPAPER = "current_wallpaper"
        const val KEY_IS_USER_LOGGED_IN = "current_wallpaper"
        private const val DARK_STATUS = "DARK_STATUS"
        const val KEY_USER_NAME = "USER_NAME"
        const val KEY_USER_TOKEN = "USER_TOKEN"
        const val KEY_USER_ID = "USER_ID"
        const val KEY_USER_PROFILE_PICTURE = "PROFILE_PICTURE"
    }
    private val sharedPreferencesKey = "shared_preference"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
    private val editor by lazy { sharedPreferences.edit() }
    private val gson = Gson()
    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_USER_LOGGED_IN, false)
    }

    fun saveIsUserLoggedIn(isUserLoggedIn: Boolean) {
        editor.apply {
            putBoolean(KEY_IS_USER_LOGGED_IN, isUserLoggedIn)
            apply()
        }
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, 0)
    }

    fun saveUserId(userId: Int) {
        editor.apply {
            putInt(KEY_USER_ID, userId)
            apply()
        }
    }

    fun getUserToken(): String {
        return sharedPreferences.getString(KEY_USER_TOKEN, "") ?: ""
    }

    fun saveUserToken(userToken: String) {
        editor.apply {
            putString(KEY_USER_TOKEN, userToken)
            apply()
        }
    }

    fun getUserProfilePicture(): String {
        return sharedPreferences.getString(KEY_USER_PROFILE_PICTURE, "") ?: ""
    }

    fun saveUserProfilePicture(userProfilePicture: String) {
        editor.apply {
            putString(KEY_USER_PROFILE_PICTURE, userProfilePicture)
            apply()
        }
    }

    fun getUserName(): String {
        return sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
    }

    fun saveUserName(userName: String) {
        editor.apply {
            putString(KEY_USER_NAME, userName)
            apply()
        }
    }
}