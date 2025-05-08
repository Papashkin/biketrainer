package com.antsfamily.domain.usecase.profile

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.model.profile.Profile
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(
    private val profileRepository: ProfilesRepository
) {

    suspend operator fun invoke(params: Profile) {
        profileRepository.insertProfile(params)
        profileRepository.setSelectedProfileName(params.name)
    }
}
