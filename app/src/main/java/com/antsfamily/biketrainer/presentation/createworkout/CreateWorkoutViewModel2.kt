package com.antsfamily.biketrainer.presentation.createworkout

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.domain.antservice.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel2 @Inject constructor(
    private val workoutRepository: WorkoutRepository,
) : BaseViewModel2() {

    private val workoutSteps: MutableList<ProgramData> = mutableListOf()

    private val _uiState =
        MutableStateFlow<CreateWorkoutState>(CreateWorkoutState.Workouts(workoutSteps))
    val uiState: StateFlow<CreateWorkoutState> = _uiState

    private val _clearFieldsEvent = MutableSharedFlow<Unit>()
    val clearFieldsEvent: SharedFlow<Unit> = _clearFieldsEvent.asSharedFlow()

    fun onPowerChanged() {
        _uiState.value = CreateWorkoutState.TextFieldsState()
        updateWorkoutStepsInUi()
    }

    fun onDurationChanged() {
        _uiState.value = CreateWorkoutState.TextFieldsState()
        updateWorkoutStepsInUi()
    }

    fun onWorkoutNameChanged() {
        _uiState.value = CreateWorkoutState.TextFieldsState()
        updateWorkoutStepsInUi()
    }

    fun onAddStepClick(power: Int, duration: String) {
        if (isDurationValid(duration)) {
            val convertedDuration = getDurationValue(duration)
            if (power <= 0 || convertedDuration <= 0L) {
                _uiState.value = CreateWorkoutState.TextFieldsState(workoutStepError = "Workout shouldn't be empty")
            } else {
                val step = ProgramData(power, convertedDuration)
                workoutSteps.add(step)
                updateWorkoutStepsInUi()
            }
        } else {
            _uiState.value = CreateWorkoutState.TextFieldsState(workoutDurationError = "Duration should be in time format: 23:59:59")
        }
    }

    fun onRemoveLastStepClick() {
        workoutSteps.removeLast()
        updateWorkoutStepsInUi()
    }

    fun onSaveClick(workoutName: String) {
        if (workoutName.isEmpty()) {
            _uiState.value = CreateWorkoutState.TextFieldsState(workoutNameError = "Workout name shouldn't be empty")
        } else {
            proceedSave(workoutName)
        }
    }

    private fun proceedSave(workoutName: String) = viewModelScope.launch {
        try {
            _uiState.value = CreateWorkoutState.Loading
            workoutRepository.insertProgram(Program(workoutName, workoutSteps))
            _uiState.value = CreateWorkoutState.TextFieldsState(null)
            workoutSteps.clear()
            updateWorkoutStepsInUi()
            _clearFieldsEvent.emit(Unit)
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.message.orEmpty())
        }
    }

    private fun updateWorkoutStepsInUi() {
        _uiState.value = CreateWorkoutState.Workouts(workoutSteps)
    }

    private fun getDurationValue(duration: String): Long {
        val (hours, minutes, seconds) = duration.mapToHoursMinutesSeconds()
        return seconds + minutes.times(60) + hours.times(3600)
    }

    private fun isDurationValid(duration: String): Boolean {
        val (hours, minutes, seconds) = duration.mapToHoursMinutesSeconds()
        return hours <= 23 && minutes <= 59 && seconds <= 59
    }

    private fun String.mapToHoursMinutesSeconds(): List<Long> = split(":").map { it.toLongOrNull().orZero() }
}
