package com.antsfamily.biketrainer.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.presentation.home.HomeViewModel2
import com.antsfamily.biketrainer.ui.common.FullScreenLoading
import com.antsfamily.biketrainer.ui.home.view.HomeScreenContentWithData
import com.antsfamily.biketrainer.ui.home.view.HomeScreenEmptyContent

interface HomeScreen {
    companion object {
        @Composable
        fun Content(navController: NavController) {
            HomeScreen(navController)
        }
    }
}

@Composable
private fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel2 = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    when (val state = uiState.value) {
        HomeState.Loading -> FullScreenLoading()
        is HomeState.EmptyContent -> HomeScreenEmptyContent(state.profileName, viewModel)
        is HomeState.ContentWithData -> HomeScreenContentWithData(
            state.profileName,
            state.workouts,
            viewModel
        )
    }

    LaunchedEffect(Unit) {
        viewModel.navigationFlow.collect {
            navController.navigate(it)
        }
    }
}
