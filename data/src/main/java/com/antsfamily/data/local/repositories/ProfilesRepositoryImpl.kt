package com.antsfamily.data.local.repositories

import com.antsfamily.data.local.ProfileStore
import com.antsfamily.data.local.database.ProfileDao
import com.antsfamily.data.model.profile.toDTO
import com.antsfamily.data.model.profile.toDomainModel
import com.antsfamily.domain.model.Profile
import com.antsfamily.domain.repository.ProfilesRepository
import javax.inject.Inject

class ProfilesRepositoryImpl @Inject constructor(
    private val dao: ProfileDao,
    private val profileStore: ProfileStore
) : ProfilesRepository {
    override fun getSelectedProfileName(): String? = profileStore.getSelectedProfile()
    override fun setSelectedProfileName(profileName: String) {
        profileStore.setSelectedProfile(profileName)
    }

    override fun getDarkModeEnabled(): Boolean =
        profileStore.getDarkModeEnabled()

    override fun setDarkModeEnabled(isEnabled: Boolean) {
        profileStore.setDarkModeEnabled(isEnabled)
    }

    override suspend fun getAllProfiles(): List<Profile> {
        val profileDTOs = dao.getAll()
        return profileDTOs.map { it.toDomainModel() }
    }
    override suspend fun getProfile(name: String): Profile? = dao.getProfile(name)?.toDomainModel()
    override suspend fun insertProfile(profile: Profile) = dao.addProfile(profile.toDTO())
    override suspend fun updateProfile(profile: Profile) = dao.updateProfile(profile.toDTO())
    override suspend fun removeProfile(profile: Profile) = dao.deleteProfile(profile.toDTO())
}
