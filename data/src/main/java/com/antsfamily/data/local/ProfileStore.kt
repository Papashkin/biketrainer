package com.antsfamily.data.local

import javax.inject.Inject

class ProfileStore @Inject constructor(
    private val sharedPrefs: SharedPrefs
) {

    fun clearAll() = sharedPrefs.clearAll()

    fun setSelectedProfile(profileName: String) = sharedPrefs.editAndCommit {
        it.putString(KEY_PROFILE_NAME, profileName)
    }

    fun getSelectedProfile(): String? =
        sharedPrefs.getPrefs().getString(KEY_PROFILE_NAME, null)

    fun setDarkModeEnabled(isEnabled: Boolean) = sharedPrefs.editAndCommit {
        it.putBoolean(KEY_PROFILE_IS_DARK_MODE, isEnabled)
    }

    fun getDarkModeEnabled(): Boolean =
        sharedPrefs.getPrefs().getBoolean(KEY_PROFILE_IS_DARK_MODE, false)

    companion object {
        private const val KEY_PROFILE_NAME = "key_profile_name"
        private const val KEY_PROFILE_IS_DARK_MODE = "key_profile_is_dark_mode"
    }
}
