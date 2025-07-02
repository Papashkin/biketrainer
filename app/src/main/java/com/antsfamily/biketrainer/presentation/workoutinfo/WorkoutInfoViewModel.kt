package com.antsfamily.biketrainer.presentation.workoutinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.presentation.createprofile.model.LoadingState
import com.antsfamily.domain.repository.WorkoutRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel(assistedFactory = WorkoutInfoViewModel.Factory::class)
class WorkoutInfoViewModel @AssistedInject constructor(
    private val workoutRepository: WorkoutRepository,
    @Assisted("id") workoutId: Int
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("id") workoutId: Int): WorkoutInfoViewModel
    }

    private val _uiState = MutableStateFlow(WorkoutInfoUiState())
    val uiState: StateFlow<WorkoutInfoUiState> = _uiState

    private val _navigateBackEvent = MutableSharedFlow<Unit>()
    val navigateBackEvent: SharedFlow<Unit> = _navigateBackEvent.asSharedFlow()

    private val _showSnackbarBackEvent = MutableSharedFlow<String>()
    val showSnackbarBackEvent: SharedFlow<String> = _showSnackbarBackEvent.asSharedFlow()

    init {
        getWorkout(workoutId)
    }

    private fun getWorkout(workoutId: Int) = viewModelScope.launch {
        workoutRepository.getWorkoutById(workoutId)?.let {

            _uiState.update { state ->
                state.copy(
                    loadingState = LoadingState.Success(it.title),
                    workout = it,
                    name = it.title,
                    duration = it.totalDuration.toString(),
                    avgPower = it.averagePower.toString()
//                    maxPower = workout.data.maxOf { it.power }.toString(),
                )
            }
        }
    }

    fun onDeleteClick() {
        deleteWorkout()
    }

    fun onRunWorkoutClick() {
        _uiState.value.name?.let {
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
            _uiState.value.workout?.let {
                workoutRepository.removeWorkout(it)
                _showSnackbarBackEvent.emit("Workout was successfully deleted")
            }
        } catch (e: Exception) {
            hideLoading()
        } finally {
            hideLoading()
        }
    }

    private fun showLoading() {
        _uiState.update { it.copy(loadingState = LoadingState.Loading) }
    }

    private fun hideLoading() {
        _uiState.update { it.copy(loadingState = LoadingState.Nothing) }
    }

    private fun navigateBack() = viewModelScope.launch {
        _navigateBackEvent.emit(Unit)
    }
}
