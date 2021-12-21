package com.antsfamily.biketrainer.presentation.profiles

import androidx.lifecycle.viewModelScope
import com.antsfamily.data.model.profile.Profile
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.domain.usecase.profile.GetAllProfilesUseCase
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ProfilesViewModel @AssistedInject constructor(
    private val getAllProfilesUseCase: GetAllProfilesUseCase
) : StatefulViewModel<ProfilesViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(): ProfilesViewModel
    }

    data class State(
        val isLoading: Boolean = true,
        val profiles: List<Profile> = emptyList(),
        val isProfilesVisible: Boolean = false,
        val isEmptyProfileVisible: Boolean = false
    )

    init {
        getProfiles()
    }

    fun onBackButtonClick() {
        navigateBack()
    }

    fun addNewProfileClick() {
        // TODO: 14.02.2021
    }

    private fun getProfiles() = viewModelScope.launch {
        getAllProfilesUseCase.run(Unit)
            .handleResult(::handleProfileSuccessResult, ::handleProfileFailureResult)
    }

    private fun handleProfileSuccessResult(profiles: List<Profile>) {
        changeState {
            it.copy(
                profiles = profiles,
                isLoading = false,
                isProfilesVisible = profiles.isNotEmpty(),
                isEmptyProfileVisible = profiles.isEmpty()
            )
        }
    }

    private fun handleProfileFailureResult(error: Error) {
        changeState {
            it.copy(
                profiles = emptyList(),
                isEmptyProfileVisible = false,
                isLoading = false,
                isProfilesVisible = false
            )
        }
    }
}
