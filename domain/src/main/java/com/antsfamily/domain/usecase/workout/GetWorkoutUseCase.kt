package com.antsfamily.domain.usecase.workout

import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.program.Program
import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
import javax.inject.Inject

class GetWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : BaseUseCase<String, Result<Program, Error>>() {

    override suspend fun run(params: String): Result<Program, Error> = try {
        val program: Program? = workoutRepository.getProgram(params)
        program?.let { Result.Success(it) }
            ?: run { Result.Failure(Error("There is no program with mentioned title")) }
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail"))
    }
}
