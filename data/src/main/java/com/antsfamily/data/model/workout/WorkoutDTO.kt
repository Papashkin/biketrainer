package com.antsfamily.data.model.workout

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.antsfamily.domain.model.IndexedWorkoutStep
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.model.WorkoutStep
import com.antsfamily.domain.model.toDuration
import kotlin.random.Random

/**
 * [WorkoutDTO] - data set class for trainings program
 * @param id - ID of program (primary key)
 * @param title - name of program
 * @param data - List of [IndexedWorkoutStepDTO]
 */
@Entity
data class WorkoutDTO(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val data: List<IndexedWorkoutStepDTO>,
)

fun Workout.toDTO(): WorkoutDTO = WorkoutDTO(
    id = Random.nextInt(),
    title = this.title,
    data = this.data.map { it.toDTO() }
)

fun IndexedWorkoutStep.toDTO(): IndexedWorkoutStepDTO {
    return IndexedWorkoutStepDTO(
        index = this.index,
        data = this.step.toDTO()
    )
}

fun WorkoutStep.toDTO(): WorkoutStepDTO = when (this) {
    is WorkoutStep.OneStep -> WorkoutStepDTO.OneStepDTO(power, duration.total)
    is WorkoutStep.Intervals -> WorkoutStepDTO.IntervalsDTO(
        power, powerDuration.total, rest, restDuration.total, reps
    )

    is WorkoutStep.CoolDown -> WorkoutStepDTO.RampStepDTO(
        startPower = startPower,
        endPower = endPower,
        duration.total
    )

    is WorkoutStep.WarmUp -> WorkoutStepDTO.RampStepDTO(
        startPower = startPower,
        endPower = endPower,
        duration.total
    )
}

fun IndexedWorkoutStepDTO.toDomainModel(): IndexedWorkoutStep {
    return IndexedWorkoutStep(
        index = this.index,
        step = this.data.toDomainModel(),
    )
}

fun WorkoutStepDTO.toDomainModel(): WorkoutStep {
    return when (this) {
        is WorkoutStepDTO.IntervalsDTO -> WorkoutStep.Intervals(
            power = this.power,
            powerDuration = this.powerDuration.toDuration(),
            rest = this.rest,
            restDuration = this.restDuration.toDuration(),
            reps = this.reps,
        )

        is WorkoutStepDTO.OneStepDTO -> WorkoutStep.OneStep(
            power = this.power,
            duration = this.duration.toDuration()
        )

        is WorkoutStepDTO.RampStepDTO -> {
            if (this.startPower > this.endPower) {
                WorkoutStep.CoolDown(startPower, endPower, duration.toDuration())
            } else {
                WorkoutStep.WarmUp(startPower, endPower, duration.toDuration())
            }
        }
    }
}