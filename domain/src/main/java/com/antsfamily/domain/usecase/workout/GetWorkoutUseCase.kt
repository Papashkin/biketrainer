package com.antsfamily.domain.usecase.workout

import com.antsfamily.domain.BaseUseCase
import com.antsfamily.domain.Result
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.repository.WorkoutRepository
import javax.inject.Inject

class GetWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : BaseUseCase<String, Result<Workout, Error>>() {

    override suspend fun run(params: String): Result<Workout, Error> = try {
        val program: Workout? = workoutRepository.getWorkoutByName(params)
        program?.let { Result.Success(it) }
            ?: run { Result.Failure(Error("There is no program with mentioned title")) }
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail"))
    }
}
