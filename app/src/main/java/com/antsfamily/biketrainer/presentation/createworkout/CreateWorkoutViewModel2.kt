package com.antsfamily.biketrainer.presentation.createworkout

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutType
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

    private val _uiState = MutableStateFlow(CreateWorkoutState())
    val uiState: StateFlow<CreateWorkoutState> = _uiState

    private val _clearFieldsEvent = MutableSharedFlow<Unit>()
    val clearFieldsEvent: SharedFlow<Unit> = _clearFieldsEvent.asSharedFlow()

    fun onPowerChanged() {
        _uiState.update {
            it.copy(powerError = null)
        }
    }

    fun onDurationChanged() {
        _uiState.update {
            it.copy(durationError = null)
        }
    }

    fun onWorkoutNameChanged() {
        _uiState.update {
            it.copy(nameError = null)
        }
    }

    fun onWorkoutTypeChanged(type: WorkoutType) {
        _uiState.update {
            it.copy(workoutType = type)
        }
    }

    fun onAddStepClick(power: Int, duration: String) {
        if (isDurationValid(duration)) {
            val convertedDuration = getDurationValue(duration)
            if (power <= 0 || convertedDuration <= 0L) {
                _uiState.update {
                    it.copy(stepError = "Workout shouldn't be empty")
                }
            } else {
                val step = ProgramData(power, convertedDuration)
                _uiState.update {
                    it.copy(steps = workoutSteps.plus(step))
                }
                workoutSteps.add(step)
            }
        } else {
            _uiState.update {
                it.copy(durationError = "Duration should be in time format: 23:59:59")
            }
        }
    }

    fun onRemoveLastStepClick() {
        _uiState.update {
            it.copy(steps = workoutSteps.dropLast(1))
        }
        workoutSteps.removeLast()
    }

    fun onSaveClick(workoutName: String) {
        if (workoutName.isEmpty()) {
            _uiState.update {
                it.copy(nameError = "Workout name shouldn't be empty")
            }
        } else {
            proceedSave(workoutName)
        }
    }

    private fun proceedSave(workoutName: String) = viewModelScope.launch {
        showLoading()
        try {
            workoutRepository.insertProgram(Program(workoutName, workoutSteps))
            workoutSteps.clear()
            _uiState.update {
                it.copy(steps = emptyList())
            }
            _clearFieldsEvent.emit(Unit)
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.message.orEmpty())
        } finally {
            hideLoading()
        }
    }

    private fun getDurationValue(duration: String): Long {
        val (hours, minutes, seconds) = duration.mapToHoursMinutesSeconds()
        return seconds + minutes.times(60) + hours.times(3600)
    }

    private fun isDurationValid(duration: String): Boolean {
        if (duration.isEmpty()) return false
        val (hours, minutes, seconds) = duration.mapToHoursMinutesSeconds()
        return hours <= 23 && minutes <= 59 && seconds <= 59
    }

    private fun String.mapToHoursMinutesSeconds(): List<Long> =
        split(":").map { it.toLongOrNull().orZero() }

    private fun showLoading() {
        _uiState.update {
            it.copy(isLoading = true)
        }
    }

    private fun hideLoading() {
        _uiState.update {
            it.copy(isLoading = false)
        }
    }
}
