package com.antsfamily.domain.usecase.profile

import com.antsfamily.domain.model.Profile
import com.antsfamily.domain.repository.ProfilesRepository
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(
    private val profileRepository: ProfilesRepository
) {

    suspend operator fun invoke(params: Profile) {
        profileRepository.insertProfile(params)
        profileRepository.setSelectedProfileName(params.name)
    }
}
