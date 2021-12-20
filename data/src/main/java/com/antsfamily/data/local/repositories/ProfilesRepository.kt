package com.antsfamily.data.local.repositories

import com.antsfamily.data.local.ProfileStore
import com.antsfamily.data.local.database.ProfileDao
import com.antsfamily.data.model.profile.Profile
import com.antsfamily.data.model.profile.ProfileWithPrograms
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfilesRepositoryImpl @Inject constructor(
    private val dao: ProfileDao,
    private val profileStore: ProfileStore
) : ProfilesRepository {
    override fun getProfileWithPrograms(profileName: String): Flow<ProfileWithPrograms> =
        dao.getProfileFlow(profileName)

    override fun getSelectedProfileName(): String? = profileStore.getSelectedProfile()
    override fun setSelectedProfileName(profileName: String) {
        profileStore.saveSelectedProfile(profileName)
    }

    override suspend fun getAllProfiles(): List<Profile> = dao.getAll()
    override suspend fun getProfile(name: String): Profile? = dao.getProfile(name)
    override suspend fun insertProfile(profile: Profile) = dao.addProfile(profile)
    override suspend fun updateProfile(profile: Profile) = dao.updateProfile(profile)
    override suspend fun removeProfile(profile: Profile) = dao.deleteProfile(profile)
}

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
