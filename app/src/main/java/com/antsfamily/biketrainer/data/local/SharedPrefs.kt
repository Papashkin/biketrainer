package com.antsfamily.biketrainer.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefs @Inject constructor(context: Context) {

    private val prefsName = "com.antsfamily.biketrainer.local.shared_prefs"
    private val prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    fun editAndCommit(operation: (SharedPreferences.Editor) -> Unit): Boolean =
        with(prefs.edit()) {
            operation(this)
            commit()
        }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    fun getPrefs(): SharedPreferences = prefs
}
