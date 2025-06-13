package com.antsfamily.domain.repository

import com.antsfamily.domain.model.Profile

interface ProfilesRepository {
    fun getSelectedProfileName(): String?
    fun setSelectedProfileName(profileName: String)
    suspend fun getAllProfiles(): List<Profile>
    suspend fun getProfile(name: String): Profile?
    suspend fun insertProfile(profile: Profile)
    suspend fun updateProfile(profile: Profile)
    suspend fun removeProfile(profile: Profile)

    fun setDarkModeEnabled(isEnabled: Boolean)
    fun getDarkModeEnabled(): Boolean
}
