package com.antsfamily.domain.usecase.workout

import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
import javax.inject.Inject

class RemoveWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : BaseUseCase<String, Result<Unit, Error>>() {

    override suspend fun run(params: String): Result<Unit, Error> = try {
        val workout = workoutRepository.getProgram(params)
        workout?.let {
            workoutRepository.removeProgram(it)
            Result.Success(Unit)
        } ?: Result.Failure(Error("There is nothing to delete"))
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail :("))
    }
}
