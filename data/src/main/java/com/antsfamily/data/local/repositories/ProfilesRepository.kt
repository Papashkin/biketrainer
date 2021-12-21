package com.antsfamily.data.local.repositories

import com.antsfamily.data.model.profile.Profile
import com.antsfamily.data.model.profile.ProfileWithPrograms
import kotlinx.coroutines.flow.Flow

interface ProfilesRepository {
    fun getProfileWithPrograms(profileName: String): Flow<ProfileWithPrograms>
    fun getSelectedProfileName(): String?
    fun setSelectedProfileName(profileName: String)
    suspend fun getAllProfiles(): List<Profile>
    suspend fun getProfile(name: String): Profile?
    suspend fun insertProfile(profile: Profile)
    suspend fun updateProfile(profile: Profile)
    suspend fun removeProfile(profile: Profile)
}
