package com.antsfamily.biketrainer.ui.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.navigation.popUpToTop
import com.antsfamily.biketrainer.presentation.splash.SplashViewModel2

interface SplashScreen {
    companion object {
        @Composable
        fun Content(navController: NavController) {
            SplashScreen(navController)
        }
    }
}

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel2 = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    if (uiState.value is SplashScreenState.Loading) {
        SplashViewWithIconAndSpinner()
    }

    LaunchedEffect(Unit) {
        viewModel.navigationFlow.collect {
            navController.navigate(it) { popUpToTop(navController) }
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
