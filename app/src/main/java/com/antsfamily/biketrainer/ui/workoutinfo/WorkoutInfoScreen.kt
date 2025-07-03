package com.antsfamily.biketrainer.ui.workoutinfo

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.presentation.workoutinfo.WorkoutInfoUiState
import com.antsfamily.biketrainer.presentation.workoutinfo.WorkoutInfoViewModel
import com.antsfamily.biketrainer.ui.common.FullScreenLoading

interface WorkoutInfoScreen {
    companion object {
        @Composable
        fun Content(
            navigateBack: () -> Unit,
            snackbarHostState: SnackbarHostState,
            workoutId: Int,
        ) = WorkoutInfoScreen(
            snackbarHostState = snackbarHostState,
            workoutId = workoutId,
            navigateBack = {
                navigateBack()
            })
    }
}

@Composable
fun WorkoutInfoScreen(
    snackbarHostState: SnackbarHostState,
    workoutId: Int,
    navigateBack: () -> Unit,
    viewModel: WorkoutInfoViewModel = hiltViewModel<WorkoutInfoViewModel, WorkoutInfoViewModel.Factory> {
        it.create(workoutId)
    }
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        //TODO scan implementation
    }

    LaunchedEffect(Unit) {
        viewModel.navigateBackEvent.collect { navigateBack() }
    }

    LaunchedEffect(Unit) {
        viewModel.showSnackbarBackEvent.collect {
            val snackbarResult =
                snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            when (snackbarResult) {
                SnackbarResult.Dismissed -> viewModel.onDeleteSnackbarDismissed()
                else -> {
                    /* no-op */
                }
            }
        }
    }

    LaunchedEffect(Unit) {
//        TODO show dialog implementation
//        viewModel.showDeviceDialogEvent.observe(viewLifecycleOwner, EventObserver {
//            if (it != null) showDialog(it.first, it.second)
//        })
    }




    when (val state = uiState.value) {
        is WorkoutInfoUiState.Loading -> FullScreenLoading()
        is WorkoutInfoUiState.Content -> {
            WorkoutInfoContentScreen(
                workout = state.workout,
                onNavigationBackClick = { navigateBack() },
                onRunWorkoutClick = { viewModel.onRunWorkoutClick() },
                onSearchSensorsClick = { viewModel.onSearchSensorsClick() },
                onDeleteClick = { viewModel.onDeleteClick() }
            )
        }
    }
}
