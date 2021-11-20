package com.antsfamily.biketrainer.domain.usecase

import com.antsfamily.biketrainer.data.local.repositories.ProfilesRepository
import com.antsfamily.biketrainer.domain.BaseUseCase
import com.antsfamily.biketrainer.domain.Result
import javax.inject.Inject

class GetSelectedProfileNameUseCase @Inject constructor(
    private val repository: ProfilesRepository
) : BaseUseCase<Unit, Result<String?, Error>>() {

    override suspend fun run(params: Unit): Result<String?, Error> = try {
        Result.Success(repository.getSelectedProfileName())
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail"))
    }
}
