package com.antsfamily.domain.usecase

import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
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
