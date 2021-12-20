package com.antsfamily.domain.usecase

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.model.profile.Profile
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
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
