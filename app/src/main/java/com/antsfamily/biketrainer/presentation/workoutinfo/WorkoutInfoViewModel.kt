package com.antsfamily.biketrainer.presentation.workoutinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.domain.model.Workout
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

    private val _uiState = MutableStateFlow<WorkoutInfoUiState>(WorkoutInfoUiState.Loading)
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

            _uiState.value = WorkoutInfoUiState.Content(
                workout = it,
                sensors = emptyList()
            )
        }
    }

    fun onDeleteClick() {
        (_uiState.value as? WorkoutInfoUiState.Content)?.let {
            deleteWorkout(it.workout)
        }
    }

    fun onRunWorkoutClick() {
        (_uiState.value as? WorkoutInfoUiState.Content)?.let {
            // TODO: implement the navigation to workout screen
            // it.workout.id
        }
    }

    fun onSearchSensorsClick() {
        //TODO:
    }

    fun onDeleteSnackbarDismissed() {
//        hideLoading()
        navigateBack()
    }

    private fun deleteWorkout(workout: Workout) = viewModelScope.launch {
        try {
//            showLoading()
            workoutRepository.removeWorkout(workout)
            _showSnackbarBackEvent.emit("Workout was successfully deleted")
        } catch (e: Exception) {
//            hideLoading()
        } finally {
//            hideLoading()
        }
    }

//    private fun showLoading() {
//        _uiState.update { it.copy(loadingState = LoadingState.Loading) }
//    }

//    private fun hideLoading() {
//        _uiState.update { it.copy(loadingState = LoadingState.Nothing) }
//    }

    private fun navigateBack() = viewModelScope.launch {
        _navigateBackEvent.emit(Unit)
    }
}
