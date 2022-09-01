package com.antsfamily.biketrainer.presentation.workoutinfo

import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.presentation.createprofile.model.LoadingState
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.program.Program
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutInfoViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : BaseViewModel2() {

    private val _uiState = MutableStateFlow(WorkoutInfoUiState())
    val uiState: StateFlow<WorkoutInfoUiState> = _uiState

    private var workout: Program? = null

    fun getWorkout(workoutName: String) = viewModelScope.launch {
        workoutRepository.getProgram(workoutName)?.let { workout ->
            this@WorkoutInfoViewModel.workout = workout
            _uiState.update { state ->
                state.copy(
                    loadingState = LoadingState.Success(workout.title),
                    program = workout.data,
                    programName = workoutName,
                    duration = workout.data.sumOf { it.duration }.fullTimeFormat(),
                    maxPower = workout.data.maxOf { it.power }.toString(),
                    avgPower = workout.data.sumOf { it.power }.div(workout.data.size).toString()
                )
            }
        }
    }

    fun onDeleteClick() {
        deleteWorkout()
    }

    fun onRunWorkoutClick() {
        _uiState.value.programName?.let {
            // TODO: implement the navigation to workout screen
        }
    }

    fun onSearchSensorsClick() {
        //TODO:
    }

    fun onDeleteSnackbarDismissed() {
        hideLoading()
        navigateBack()
    }

    private fun deleteWorkout() = viewModelScope.launch {
        try {
            showLoading()
            workout?.let {
                workoutRepository.removeProgram(it)
                showSnackbar("Workout was successfully deleted")
            }
        } catch (e: Exception) {
            hideLoading()
        }
    }

    private fun showLoading() {
        _uiState.update { it.copy(loadingState = LoadingState.Loading) }
    }

    private fun hideLoading() {
        _uiState.update { it.copy(loadingState = LoadingState.Nothing) }
    }
}
