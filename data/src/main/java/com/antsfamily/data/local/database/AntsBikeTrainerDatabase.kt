package com.antsfamily.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.antsfamily.data.model.profile.ProfileDTO
import com.antsfamily.data.model.workout.WorkoutDTO

@Database(entities = [ProfileDTO::class, WorkoutDTO::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AntsBikeTrainerDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun workoutDao(): WorkoutDao
}




