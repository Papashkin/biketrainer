package com.antsfamily.biketrainer.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.splash.SplashViewModel2
import com.antsfamily.biketrainer.ui.util.Padding

interface SplashScreen {
    companion object {
        @Composable
        fun Content(
            onNavigateToHome: () -> Unit,
            onNavigateToCreateProfile: () -> Unit
        ) {
            SplashScreen(
                onNavigateToHome = onNavigateToHome,
                onNavigateToCreateProfile = onNavigateToCreateProfile
            )
        }
    }
}

@Composable
fun SplashScreen(
    viewModel: SplashViewModel2 = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToCreateProfile: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    if (uiState.value is SplashScreenState.Loading) {
        SplashViewWithIconAndSpinner()
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToHomeFlow.collect {
            onNavigateToHome()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.navigateToCreateProfileFlow.collect {
            onNavigateToCreateProfile()
        }
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
            Image(
                painterResource(id = R.drawable.ic_app_icon),
                contentDescription = null,
            )
            CircularProgressIndicator(modifier = Modifier.padding(top = Padding.small))
        }
    }
}
