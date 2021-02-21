package com.antsfamily.biketrainer.presentation.home

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.data.models.profile.Profile
import com.antsfamily.biketrainer.data.models.profile.ProfileWithPrograms
import com.antsfamily.biketrainer.data.models.program.Program
import com.antsfamily.biketrainer.domain.usecase.SubscribeToProfileWithProgramsUseCase
import com.antsfamily.biketrainer.navigation.HomeToCreateProgram
import com.antsfamily.biketrainer.navigation.HomeToProgramInfo
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    subscribeToProfileWithProgramsUseCase: SubscribeToProfileWithProgramsUseCase,
) : StatefulViewModel<HomeViewModel.State>(State()) {

    data class State(
        val dateTime: String? = null,
        val isProgramsLoading: Boolean = true,
        val isProgramsVisible: Boolean = false,
        val isEmptyProgramsVisible: Boolean = false,
        val profile: Profile? = null
    )

    val profileWithProgramsFlow = subscribeToProfileWithProgramsUseCase(Unit)
        .onStart { showLoading() }
        .onEach { handleProfileWithPrograms(it) }
        .asLiveData(viewModelScope.coroutineContext)

    init {
        getDateTime()
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

    private fun getDateTime() {
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_FULL))
        changeState { it.copy(dateTime = date) }
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

    private fun handleProfileWithPrograms(profileWithPrograms: ProfileWithPrograms) {
        changeState {
            it.copy(
                isProgramsLoading = false,
                isProgramsVisible = profileWithPrograms.programs.isNotEmpty(),
                isEmptyProgramsVisible = profileWithPrograms.programs.isEmpty(),
                profile = profileWithPrograms.profile
            )
        }
    }

    companion object {
        private const val DATE_FORMAT_FULL = "EEEE, MMMM dd, yyyy"
    }
}
