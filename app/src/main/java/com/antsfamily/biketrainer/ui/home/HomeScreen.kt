package com.antsfamily.biketrainer.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.presentation.home.HomeViewModel2
import com.antsfamily.biketrainer.ui.common.FullScreenLoading
import com.antsfamily.biketrainer.ui.home.view.HomeScreenContentWithData
import com.antsfamily.biketrainer.ui.home.view.HomeScreenEmptyContent

interface HomeScreen {
    companion object {
        @Composable
        fun Content(onNavigate: (String) -> Unit) {
            HomeScreen(onNavigate = onNavigate)
        }
    }
}

@Composable
private fun HomeScreen(
    viewModel: HomeViewModel2 = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    when (val state = uiState.value) {
        HomeState.Loading -> FullScreenLoading()
        is HomeState.EmptyContent -> HomeScreenEmptyContent(state.profileName) {
            viewModel.onCreateWorkoutClick()
        }
        is HomeState.ContentWithData -> HomeScreenContentWithData(
            profileName = state.profileName,
            workouts = state.workouts,
            onWorkoutClick = {
                viewModel.onWorkoutClick(it)
            },
            onCreateWorkoutClick = {
                viewModel.onCreateWorkoutClick()
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.navigationFlow.collect {
            onNavigate(it)
        }
    }
}
