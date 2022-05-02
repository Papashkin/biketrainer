package com.antsfamily.biketrainer.ui.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    viewModel: SplashViewModel2 = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState(SplashScreenState.Loading)
    when (val state = uiState.value) {
        SplashScreenState.Loading -> SplashViewWithIconAndSpinner()
        is SplashScreenState.NavigateToMain -> navigateToMain(state.profileName)
        SplashScreenState.NavigateToCreateProfile -> navigateToCreateAccount()
    }
}

@Composable
fun SplashViewWithIconAndSpinner() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = R.drawable.ic_cycling_road,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                modifier = Modifier.size(100.dp)
            )
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}
