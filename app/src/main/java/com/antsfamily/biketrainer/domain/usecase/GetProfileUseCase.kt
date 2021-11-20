package com.antsfamily.biketrainer.domain.usecase

import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepository
import com.antsfamily.biketrainer.data.models.profile.Profile
import com.antsfamily.biketrainer.domain.BaseUseCase
import com.antsfamily.biketrainer.domain.Result
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfilesRepository
) : BaseUseCase<String, Result<Profile?, Error>>() {

    override suspend fun run(params: String): Result<Profile?, Error> = try {
        Result.Success(profileRepository.getProfile(params))
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail"))
    }
}
