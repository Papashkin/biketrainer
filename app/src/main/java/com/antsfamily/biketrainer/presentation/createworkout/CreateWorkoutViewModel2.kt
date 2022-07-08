package com.antsfamily.biketrainer.presentation.createworkout

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel2 @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val profilesRepository: ProfilesRepository,
) : BaseViewModel2() {

    private val workoutSteps: MutableList<ProgramData> = mutableListOf()

    private val _uiState =
        MutableStateFlow<CreateWorkoutState>(CreateWorkoutState.Workouts(workoutSteps))
    val uiState: StateFlow<CreateWorkoutState> = _uiState

    private val _clearFieldsEvent = MutableSharedFlow<Unit>()
    val clearFieldsEvent: SharedFlow<Unit> = _clearFieldsEvent.asSharedFlow()

    fun onPowerChanged() {

    }

    fun onDurationChanged() {

    }

    fun onWorkoutNameChanged() {
        _uiState.value = CreateWorkoutState.TextFieldsState(null)
    }

    fun onAddStepClick(workoutName: String, power: Int, duration: Long) {
        if (workoutName.isEmpty()) {
            _uiState.value = CreateWorkoutState.TextFieldsState("Workout name shouldn't be empty")
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

    fun onSaveClick() = viewModelScope.launch {
        try {
            val profileName = profilesRepository.getSelectedProfileName()
            _uiState.value = CreateWorkoutState.Loading
            workoutRepository.insertProgram(Program("", workoutSteps))
            _uiState.value = CreateWorkoutState.TextFieldsState(null)
            workoutSteps.clear()
            updateWorkoutStepsInUi()
            _clearFieldsEvent.emit(Unit)
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.message.orEmpty())
        }
    }

    fun onCancelClick() {
        navigateBack()
    }

    private fun updateWorkoutStepsInUi() {
        _uiState.value = CreateWorkoutState.Workouts(workoutSteps)
    }
}
