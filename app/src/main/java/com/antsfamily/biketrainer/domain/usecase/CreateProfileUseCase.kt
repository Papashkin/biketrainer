package com.antsfamily.biketrainer.domain.usecase

import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepository
import com.antsfamily.biketrainer.data.models.profile.Profile
import com.antsfamily.biketrainer.domain.BaseUseCase
import com.antsfamily.biketrainer.domain.Result
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(
    private val profileRepository: ProfilesRepository
) : BaseUseCase<Profile, Result<String, Error>>() {

    override suspend fun run(params: Profile): Result<String, Error> = try {
        profileRepository.insertProfile(params)
        profileRepository.setSelectedProfileName(params.name)
        Result.Success(params.name)
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail"))
    }
}
