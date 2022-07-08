package com.antsfamily.biketrainer.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BuildConfig
import com.antsfamily.biketrainer.navigation.HomeToCreateProgram
import com.antsfamily.biketrainer.navigation.HomeToProgramInfo
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.data.local.repositories.WorkoutRepository
import com.antsfamily.data.model.profile.Profile
import com.antsfamily.data.model.program.Program
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeViewModel @AssistedInject constructor(
    private val workoutRepository: WorkoutRepository,
    @Assisted private val profileName: String,
) : StatefulViewModel<HomeViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(profileName: String): HomeViewModel
    }

    data class State(
        val dateTime: String? = null,
        val appVersion: String? = null,
        val isProgramsLoading: Boolean = true,
        val isProgramsVisible: Boolean = false,
        val isEmptyProgramsVisible: Boolean = false,
        val profile: Profile? = null,
        val programs: List<Program> = emptyList(),
    )

    init {
        getProfileWithPrograms()
        getDateTimeAndAppVersion()
    }

    fun onSettingsClick() {
        // TODO: later
    }

    fun onProgramClick(item: Program) {
        navigateTo(HomeToProgramInfo(item.title))
    }

    fun onCreateProgramClick() {
        navigateTo(HomeToCreateProgram)
    }

    private fun getProfileWithPrograms() = viewModelScope.launch {
        workoutRepository.programs
            .onStart { showLoading() }
            .onCompletion { Log.e("ProgramsRepo", "!!!! COMPLETE !!!!") }
            .collect { handleProfileWithPrograms(it) }
    }

    private fun getDateTimeAndAppVersion() {
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_FULL))
        changeState {
            it.copy(
                dateTime = date,
                appVersion = BuildConfig.VERSION_NAME
            )
        }
    }

    private fun showLoading() {
        changeState {
            it.copy(
                isProgramsLoading = true,
                isProgramsVisible = false,
                isEmptyProgramsVisible = false
            )
        }
    }

    private fun handleProfileWithPrograms(programs: List<Program>) {
        changeState {
            it.copy(
                isProgramsLoading = false,
                isProgramsVisible = programs.isNotEmpty(),
                isEmptyProgramsVisible = programs.isEmpty(),
                programs = programs,
            )
        }
    }

    companion object {
        private const val DATE_FORMAT_FULL = "EEEE, MMMM dd, yyyy"
    }
}
