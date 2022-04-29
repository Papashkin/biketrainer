package com.antsfamily.biketrainer.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.antsfamily.biketrainer.R

interface SplashScreen {
    companion object {
        @Composable
        fun Content(
            navigateToMain: (profile: String) -> Unit,
            navigateToCreateAccount: () -> Unit
        ) {
            SplashScreen(navigateToMain, navigateToCreateAccount)
        }
    }
}

@Composable
fun SplashScreen(
    navigateToMain: (profile: String) -> Unit,
    navigateToCreateAccount: () -> Unit,
    viewModel: SplashViewModel2 = viewModel(),
) {
    when (val state = viewModel.uiState.collectAsState().value) {
        SplashScreenState.Loading -> SplashViewWithIconAndSpinner()
        is SplashScreenState.NavigateToMain -> {
            navigateToMain(state.profileName)
        }
        SplashScreenState.NavigateToCreateProfile -> {
            navigateToCreateAccount()
        }
    }
}

@Composable
fun SplashViewWithIconAndSpinner() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(model = R.drawable.ic_cycling_road, contentDescription = null)
            CircularProgressIndicator()
        }
    }
}
