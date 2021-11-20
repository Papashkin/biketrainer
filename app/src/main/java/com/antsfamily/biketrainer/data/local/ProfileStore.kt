package com.antsfamily.biketrainer.data.local

import javax.inject.Inject

class ProfileStore @Inject constructor(
    private val sharedPrefs: SharedPrefs
) {

    fun clearAll() = sharedPrefs.clearAll()

    fun saveSelectedProfile(profileName: String) = sharedPrefs.editAndCommit {
        it.putString(KEY_PROFILE_NAME, profileName)
    }

    fun getSelectedProfile(): String? = sharedPrefs.getPrefs().getString(KEY_PROFILE_NAME, null)

    companion object {
        private const val KEY_PROFILE_NAME = "key_profile_name"
    }
}
