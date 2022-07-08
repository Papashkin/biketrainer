package com.antsfamily.biketrainer.presentation.home

import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.navigation.Screen
import com.antsfamily.biketrainer.ui.home.HomeState
import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.program.Program
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel2 @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val workoutRepository: WorkoutRepository,
) : BaseViewModel2() {

    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)
    val uiState: StateFlow<HomeState> = _uiState

    init {
        getContent()
    }

    fun onCreateWorkoutClick() {
        navigateTo(Screen.CreateWorkout)
    }

    fun onWorkoutClick(workout: Program) {
        //TODO add navigation to Workout info screen
    }

    private fun getContent() = viewModelScope.launch {
        val workouts = workoutRepository.getAllPrograms()
        profilesRepository.getSelectedProfileName()?.let {
            _uiState.value = if (workouts.isEmpty()) {
                HomeState.EmptyContent(it)
            } else {
                HomeState.ContentWithData(it, workouts)
            }
        }
    }
}
