package com.antsfamily.biketrainer.presentation.splash

import android.os.Handler
import android.os.Looper
import com.antsfamily.biketrainer.domain.Result
import com.antsfamily.biketrainer.domain.usecase.GetSelectedProfileNameUseCase
import com.antsfamily.biketrainer.navigation.SplashToCreateProfile
import com.antsfamily.biketrainer.navigation.SplashToHome
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SplashViewModel @AssistedInject constructor(
    private val getSelectedProfileNameUseCase: GetSelectedProfileNameUseCase
) : StatefulViewModel<SplashViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(): SplashViewModel
    }

    data class State(
        val isLoading: Boolean = false
    )

    fun onResume() {
        showLoading()
        Handler(Looper.getMainLooper()).postDelayed(::getSelectedProfile, DELAY)
    }

    private fun getSelectedProfile() {
        getSelectedProfileNameUseCase(Unit, ::handleSelectedProfileResult)
    }

    private fun handleSelectedProfileResult(result: Result<String?, Error>) {
        when (result) {
            is Result.Success -> handleSuccessResult(result.successData)
            else -> navigateToCreateProfile()
        }
    }

    private fun handleSuccessResult(profileName: String?) {
        profileName?.let {
            navigateToStart(it)
        } ?: run {
            navigateToCreateProfile()
        }
    }

    private fun showLoading() {
        changeState { it.copy(isLoading = true) }
    }

    private fun navigateToStart(profileName: String) {
        navigateTo(SplashToHome(profileName))
        hideLoading()
    }

    private fun navigateToCreateProfile() {
        navigateTo(SplashToCreateProfile)
        hideLoading()
    }

    private fun hideLoading() {
        changeState { it.copy(isLoading = false) }
    }

    companion object {
        private const val DELAY = 1000L
    }
}
