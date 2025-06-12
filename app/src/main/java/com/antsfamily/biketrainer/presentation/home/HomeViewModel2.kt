package com.antsfamily.biketrainer.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.navigation.Screen
import com.antsfamily.biketrainer.ui.home.HomeState
import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.program.Program
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
class HomeViewModel2 @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val workoutRepository: WorkoutRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)
    val uiState: StateFlow<HomeState> = _uiState

    private val _navigationFlow = MutableSharedFlow<String>()
    val navigationFlow: SharedFlow<String> = _navigationFlow.asSharedFlow()

    private var username: String? = null

    init {
        getContent()
    }

    fun onCreateWorkoutClick() {
        navigateTo(Screen.CreateWorkout)
    }

    fun onWorkoutClick(workout: Program) {
        navigateTo(Screen.WorkoutInfo, workout.title)
    }

    private fun getContent() = viewModelScope.launch {
        profilesRepository.getSelectedProfileName()?.let {
            username = it
            getWorkouts()
        }
    }

    private fun getWorkouts() = viewModelScope.launch {
        workoutRepository.programs
            .onStart { /* no-op */ }
            .onCompletion { Log.e("ProgramsRepo", "!!!! COMPLETE !!!!") }
            .collect { handleWorkouts(it) }
    }

    private fun handleWorkouts(workouts: List<Program>) {
        username?.let {
            _uiState.value = if (workouts.isEmpty()) {
                HomeState.EmptyContent(it)
            } else {
                HomeState.ContentWithData(it, workouts)
            }
        }
    }

    private fun navigateTo(screen: Screen) = viewModelScope.launch {
        _navigationFlow.emit(screen.route)
    }

    private fun navigateTo(screen: Screen, argument: String) = viewModelScope.launch {
        _navigationFlow.emit("${screen.route.substringBefore("/")}/$argument")
    }
}
