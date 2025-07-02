package com.antsfamily.biketrainer.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.presentation.home.HomeViewModel
import com.antsfamily.biketrainer.ui.common.FullScreenLoading
import com.antsfamily.biketrainer.ui.home.view.HomeScreenContentWithData
import com.antsfamily.biketrainer.ui.home.view.HomeScreenEmptyContent

interface HomeScreen {
    companion object {
        @Composable
        fun Content(
            navigateToCreateWorkout: () -> Unit,
            navigateToEditWorkout: (Int) -> Unit,
            navigateToWorkoutInfo: (Int) -> Unit
        ) {
            HomeScreen(
                navigateToCreateWorkout = navigateToCreateWorkout,
                navigateToEditWorkout = navigateToEditWorkout,
                navigateToWorkoutInfo = navigateToWorkoutInfo
            )
        }
    }
}

@Composable
private fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToCreateWorkout: () -> Unit,
    navigateToEditWorkout: (Int) -> Unit,
    navigateToWorkoutInfo: (Int) -> Unit
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
            onWorkoutClick = { viewModel.onWorkoutClick(it) },
            onEditWorkoutClick = { viewModel.onEditWorkoutClick(it) },
            onCreateWorkoutClick = { viewModel.onCreateWorkoutClick() }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.navigationToEditWorkout.collect { navigateToEditWorkout(it) }
    }
    LaunchedEffect(Unit) {
        viewModel.navigationToWorkoutInfo.collect { navigateToWorkoutInfo(it) }
    }
    LaunchedEffect(Unit) {
        viewModel.navigationToCreateWorkout.collect { navigateToCreateWorkout() }
    }
}
