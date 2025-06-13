package com.antsfamily.domain.usecase.workout

import com.antsfamily.domain.Result
import com.antsfamily.domain.model.IndexedWorkoutStep
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.repository.WorkoutRepository
import javax.inject.Inject

class SaveWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {

    suspend operator fun invoke(
        name: String,
        data: List<IndexedWorkoutStep>
    ): Result<Unit, Error> = try {
        val workout = Workout(name, data)
        workoutRepository.insertWorkout(workout)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Failure(Error("Epic fail :("))
    }
}
