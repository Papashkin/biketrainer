package com.antsfamily.biketrainer.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.navigation.Screen
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

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()

    init {
        getContent()
    }

    fun onCreateWorkoutClick() {
        navigateTo(Screen.CreateWorkout)
    }

    fun onWorkoutClick(workout: Workout) {
        navigateTo(Screen.WorkoutInfo, workout.title)
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

    private fun navigateTo(screen: Screen) = viewModelScope.launch {
        _navigationFlow.emit(screen.route)
    }

    private fun navigateTo(screen: Screen, argument: String) = viewModelScope.launch {
        _navigationFlow.emit("${screen.route.substringBefore("/")}/$argument")
    }
}
