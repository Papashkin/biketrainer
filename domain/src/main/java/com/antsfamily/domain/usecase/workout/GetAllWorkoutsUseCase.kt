package com.antsfamily.domain.usecase.workout

import com.antsfamily.domain.Result
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.repository.WorkoutRepository
import javax.inject.Inject

class GetAllWorkoutsUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {

    suspend operator fun invoke(): Result<List<Workout>, Error> = try {
        val workouts = workoutRepository.getAllWorkouts()
        if (workouts.isNotEmpty()) {
            Result.Success(workouts)
        } else {
            Result.Failure(Error("There is no any workout"))
        }
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail"))
    }
}