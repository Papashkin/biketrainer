package com.antsfamily.data.local.database

import androidx.room.TypeConverter
import com.antsfamily.data.model.workout.IndexedWorkoutStepDTO
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object WorkoutStepSerializer {
    private val json = Json { classDiscriminator = "type"; ignoreUnknownKeys = true }

    fun serialize(steps: List<IndexedWorkoutStepDTO>): String {
        return json.encodeToString(ListSerializer(IndexedWorkoutStepDTO.serializer()), steps)
    }

    fun deserialize(jsonStr: String): List<IndexedWorkoutStepDTO> {
        return json.decodeFromString(ListSerializer(IndexedWorkoutStepDTO.serializer()), jsonStr)
    }
}

class Converters {
    @TypeConverter
    fun fromWorkoutSteps(value: List<IndexedWorkoutStepDTO>): String =
        WorkoutStepSerializer.serialize(value)

    @TypeConverter
    fun toWorkoutSteps(value: String): List<IndexedWorkoutStepDTO> =
        WorkoutStepSerializer.deserialize(value)
}