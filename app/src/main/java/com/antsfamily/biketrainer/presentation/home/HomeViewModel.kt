package com.antsfamily.biketrainer.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.ui.home.HomeState
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.repository.ProfilesRepository
import com.antsfamily.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val workoutRepository: WorkoutRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)
    val uiState: StateFlow<HomeState> = _uiState

    private val _navigationToCreateWorkout = MutableSharedFlow<Unit>()
    val navigationToCreateWorkout: SharedFlow<Unit> = _navigationToCreateWorkout.asSharedFlow()

    private val _navigationToWorkoutInfo = MutableSharedFlow<Int>()
    val navigationToWorkoutInfo: SharedFlow<Int> = _navigationToWorkoutInfo.asSharedFlow()

    private val _navigationToEditWorkout = MutableSharedFlow<Int>()
    val navigationToEditWorkout: SharedFlow<Int> = _navigationToEditWorkout.asSharedFlow()

    init {
        getContent()
    }

    fun onCreateWorkoutClick() = viewModelScope.launch {
        _navigationToCreateWorkout.emit(Unit)
    }

    fun onWorkoutClick(workoutId: Int) = viewModelScope.launch {
        val workouts = (_uiState.value as? HomeState.ContentWithData)?.workouts.orEmpty()
        workouts.firstOrNull { it.id == workoutId }?.let {
            _navigationToWorkoutInfo.emit(it.id)
        }
    }

    fun onEditWorkoutClick(workoutId: Int) = viewModelScope.launch {
        val workouts = (_uiState.value as? HomeState.ContentWithData)?.workouts.orEmpty()
        workouts.firstOrNull { it.id == workoutId }?.let {
            _navigationToEditWorkout.emit(it.id)
        }
    }

    private fun getContent() = viewModelScope.launch {
        val username = profilesRepository.getSelectedProfileName()
        username?.let {
            getWorkouts(it)
        }
    }

    private fun getWorkouts(username: String) = viewModelScope.launch {
        workoutRepository.workouts
            .onStart { /* no-op */ }
            .onCompletion { Log.e("WorkoutsRepo", "!!!! COMPLETE !!!!") }
            .collect { handleWorkouts(username, it) }
    }

    private fun handleWorkouts(username: String, workouts: List<Workout>) {
        _uiState.value = if (workouts.isEmpty()) {
            HomeState.EmptyContent(username)
        } else {
            HomeState.ContentWithData(username, workouts)
        }
    }
}
