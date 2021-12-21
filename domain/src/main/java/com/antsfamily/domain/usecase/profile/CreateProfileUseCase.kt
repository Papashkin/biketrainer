package com.antsfamily.domain.usecase.profile

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.model.profile.Profile
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
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
