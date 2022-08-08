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

    fun onWorkoutNameChanged() {
        _uiState.update { it.copy(nameError = null) }
    }

    fun onPowerChanged() {
        _uiState.update { it.copy(powerError = null) }
    }

    fun onDurationChanged() {
        _uiState.update { it.copy(durationError = null) }
    }

    fun onPowerRestChanged() {
        _uiState.update { it.copy(powerRestError = null) }
    }

    fun onDurationRestChanged() {
        _uiState.update { it.copy(durationRestError = null) }
    }

    fun onRepeatsChanged() {
        _uiState.update { it.copy(repeatsError = null) }
    }

    fun onWorkoutTypeChanged(type: WorkoutType) {
        _uiState.update {
            it.copy(
                workoutType = type,
                nameError = null,
                stepError = null,
                powerError = null,
                durationError = null,
                powerRestError = null,
                durationRestError = null,
                repeatsError = null,
            )
        }
    }

    fun onAddStepClick(workoutItem: WorkoutItem) {
        with(workoutItem) {
            if (isStepValid(power, duration)) {
                val step = ProgramData(power, getDurationValue(duration))
                _uiState.update { it.copy(steps = workoutSteps.plus(step)) }
                workoutSteps.add(step)
            }
        }
    }

    fun onAddIntervalClick(workoutItem: WorkoutItem) {
        with(workoutItem) {
            if (isIntervalValid(power, duration, powerRest, durationRest, repeats)) {
                var count = 0
                val intervalSteps = mutableListOf<ProgramData>()
                while (count < repeats) {
                    intervalSteps.add(ProgramData(power, getDurationValue(duration)))
                    intervalSteps.add(ProgramData(powerRest, getDurationValue(durationRest)))
                    count++
                }
                _uiState.update { it.copy(steps = workoutSteps.plus(intervalSteps)) }
                workoutSteps.addAll(intervalSteps)
            }
        }
    }

    fun onRemoveLastStepClick() {
        _uiState.update { it.copy(steps = workoutSteps.dropLast(1)) }
        workoutSteps.removeLast()
    }

    fun onSaveClick(workoutName: String) {
        if (workoutName.isEmpty()) {
            _uiState.update { it.copy(nameError = "Workout name shouldn't be empty") }
        } else {
            proceedSave(workoutName)
        }
    }

    private fun proceedSave(workoutName: String) = viewModelScope.launch {
        showLoading()
        try {
            workoutRepository.insertProgram(Program(workoutName, workoutSteps))
            workoutSteps.clear()
            _uiState.update { it.copy(steps = emptyList()) }
            _clearFieldsEvent.emit(Unit)
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.message.orEmpty())
        } finally {
            hideLoading()
        }
    }

    private fun isIntervalValid(
        power: Int,
        duration: String,
        powerRest: Int,
        durationRest: String,
        repeats: Int
    ): Boolean {
        val isPowerRestValid = powerRest > 0
        val isDurationRestValid = isDurationValid(durationRest)
        val isRepeatsValid = repeats > 1
        if (!isPowerRestValid) {
            _uiState.update { it.copy(powerRestError = "Power should be bigger than 0") }
        }
        if (!isDurationRestValid) {
            _uiState.update {
                it.copy(durationRestError = "Duration should be in time format: 23:59:59")
            }
        }
        if (!isRepeatsValid) {
            _uiState.update { it.copy(repeatsError = "Interval should contain more than 1 step") }
        }
        return isStepValid(
            power,
            duration
        ) && isPowerRestValid && isDurationRestValid && isRepeatsValid
    }

    private fun isStepValid(power: Int, duration: String): Boolean {
        val isPowerValid = power > 0
        val isDurationValid = isDurationValid(duration)
        if (!isPowerValid) {
            _uiState.update { it.copy(powerError = "Power should be bigger than 0") }
        }
        if (!isDurationValid) {
            _uiState.update {
                it.copy(durationError = "Duration should be in time format: 23:59:59")
            }
        }
        return isPowerValid && isDurationValid
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
