package com.antsfamily.biketrainer.presentation.createworkout

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter
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

    fun onAddStepClick(power: Int, duration: Long) {
        if (power <= 0 || duration <= 0L) {
            _uiState.value = CreateWorkoutState.TextFieldsState(workoutStepError = "Workout shouldn't be empty")
        } else {
            val step = ProgramData(power, duration)
            workoutSteps.add(step)
            updateWorkoutStepsInUi()
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

    fun onCancelClick() {
        navigateBack()
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

    private fun getTimestamp() = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

    private fun updateWorkoutStepsInUi() {
        _uiState.value = CreateWorkoutState.Workouts(workoutSteps)
    }
}
