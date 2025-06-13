package com.antsfamily.domain.repository

import com.antsfamily.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun getAllWorkouts(): List<Workout>
    val workouts: Flow<List<Workout>>
    suspend fun getWorkoutByName(name: String): Workout?
    suspend fun insertWorkout(workout: Workout)
    suspend fun updateWorkout(workout: Workout)
    suspend fun removeWorkout(workout: Workout)
}
