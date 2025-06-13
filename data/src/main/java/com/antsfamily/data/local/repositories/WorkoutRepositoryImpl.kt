package com.antsfamily.data.local.repositories

import com.antsfamily.data.local.database.WorkoutDao
import com.antsfamily.data.model.workout.toDTO
import com.antsfamily.data.model.workout.toDomainModel
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(private val dao: WorkoutDao) : WorkoutRepository {

    override suspend fun getAllWorkouts(): List<Workout> {
        val data = dao.getAll()
        val workouts = data.map {
            Workout(title = it.title, data = it.data.map { step -> step.toDomainModel() })
        }
        return workouts
    }

    override val workouts: Flow<List<Workout>> = dao.getWorkouts().map { workout ->
        workout.map {
            Workout(title = it.title, data = it.data.map { step -> step.toDomainModel() })
        }
    }

    override suspend fun getWorkoutByName(name: String): Workout? {
        val workoutDTO = dao.getWorkoutByTitle(name)
        return workoutDTO?.let {
            Workout(title = it.title, data = it.data.map { step -> step.toDomainModel() })
        }
    }

    override suspend fun insertWorkout(workout: Workout) = dao.insertWorkout(workout.toDTO())
    override suspend fun updateWorkout(workout: Workout) = dao.updateWorkout(workout.toDTO())
    override suspend fun removeWorkout(workout: Workout) = dao.deleteWorkout(workout.toDTO())
}
