package com.antsfamily.biketrainer.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.splash.SplashViewModel
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
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToCreateProfile: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    (uiState.value as? SplashScreenState.Loading)?.let {
        SplashViewWithIconAndSpinner(it.isDarkTheme)
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
    LaunchedEffect(Unit) {
        viewModel.navigateToSelectProfileFlow.collect {
            //TODO Handle multiple profiles if needed (add new screen with profile selection)
        }
    }
}

@Composable
fun SplashViewWithIconAndSpinner(isDarkTheme: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Jetpack Compose can't work with drawable-night directory, so needs this workaround
            Image(
                imageVector = if (isDarkTheme) {
                    ImageVector.vectorResource(R.drawable.ic_app_icon_dark)
                } else {
                    ImageVector.vectorResource(R.drawable.ic_app_icon_light)
                },
                contentDescription = null
            )
            CircularProgressIndicator(modifier = Modifier.padding(top = Padding.medium))
        }
    }
}
