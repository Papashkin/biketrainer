package com.antsfamily.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.antsfamily.data.model.workout.WorkoutDTO
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WorkoutDao {
    @Query("SELECT * from workoutdto")
    abstract suspend fun getAll(): List<WorkoutDTO>

    @Query("SELECT * from workoutdto")
    abstract fun getWorkouts(): Flow<List<WorkoutDTO>>

    @Query("Select * from workoutdto where title = :title")
    abstract suspend fun getWorkoutByTitle(title: String): WorkoutDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkout(workout: WorkoutDTO)

    @Update
    abstract suspend fun updateWorkout(workout: WorkoutDTO)

    @Delete
    abstract suspend fun deleteWorkout(workout: WorkoutDTO)
}
