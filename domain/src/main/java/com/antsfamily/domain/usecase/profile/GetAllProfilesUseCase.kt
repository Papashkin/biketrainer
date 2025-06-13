package com.antsfamily.domain.usecase.profile

import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
import com.antsfamily.domain.model.Profile
import com.antsfamily.domain.repository.ProfilesRepository
import javax.inject.Inject

class GetAllProfilesUseCase @Inject constructor(
    private val profileRepository: ProfilesRepository
) : BaseUseCase<Unit, Result<List<Profile>, Error>>() {

    override suspend fun run(params: Unit): Result<List<Profile>, Error> = try {
        Result.Success(profileRepository.getAllProfiles())
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail"))
    }
}
