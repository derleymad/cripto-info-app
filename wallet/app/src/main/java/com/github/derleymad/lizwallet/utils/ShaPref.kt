package com.github.derleymad.lizwallet.utils

import android.content.Context
import android.preference.PreferenceManager

private const val PREF_LOGIN_NAME = "loginName"

object LoginPreferences {

    fun getStoredName(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_LOGIN_NAME, "testExample")!!
    }

    fun setStoredName(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_LOGIN_NAME, query)
            .apply()
    }
}