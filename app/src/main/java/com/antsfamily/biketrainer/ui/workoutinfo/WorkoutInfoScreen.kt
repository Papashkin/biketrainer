package com.antsfamily.biketrainer.ui.workoutinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.createprofile.model.LoadingState
import com.antsfamily.biketrainer.presentation.createworkout.WorkoutItem
import com.antsfamily.biketrainer.presentation.workoutinfo.WorkoutInfoUiState
import com.antsfamily.biketrainer.presentation.workoutinfo.WorkoutInfoViewModel
import com.antsfamily.biketrainer.ui.common.FullScreenLoading
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.TextStyles
import com.antsfamily.biketrainer.util.orEmpty
import com.antsfamily.domain.antservice.orZero


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

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Padding.large)
        ) {
            Text(
                text = uiState.value.name.orEmpty(),
                fontSize = FontSize.H4,
                modifier = Modifier
                    .padding(top = Padding.huge, start = Padding.large, end = Padding.large)
                    .fillMaxWidth()
            )

            if (uiState.value.loadingState is LoadingState.Loading) {
                FullScreenLoading()
            } else {
                WorkoutContentView(uiState.value, viewModel)
            }
        }
    }
}

@Composable
fun WorkoutContentView(
    state: WorkoutInfoUiState,
    viewModel: WorkoutInfoViewModel
) {
    Row(
        modifier = Modifier.padding(top = Padding.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(3f)
                .padding(top = Padding.x_small, start = Padding.small)
        ) {
            Text(
                text = stringResource(
                    id = R.string.compose_workout_info_time,
                    state.duration ?: "-"
                ),
                style = TextStyles.body1(),
                modifier = Modifier.padding(Padding.tiny)
            )
            Text(
                text = stringResource(
                    id = R.string.compose_workout_info_average_power,
                    state.avgPower?.toIntOrNull().orZero()
                ),
                style = TextStyles.body1(),
                modifier = Modifier.padding(Padding.tiny)
            )
            Text(
                text = stringResource(
                    id = R.string.compose_workout_info_max_power,
                    state.maxPower ?: "-"
                ),
                style = TextStyles.body1(),
                modifier = Modifier.padding(Padding.tiny)
            )
        }
    }


    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = Padding.small)
    )

    Text(
        text = "Sensors",
        style = TextStyle(fontSize = FontSize.H5, fontWeight = FontWeight.Medium),
        modifier = Modifier.padding(Padding.tiny)
    )

//    LazyColumn(content = {})

    Divider(
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = Padding.small)
    )

    LoadingButton(
        onClick = { viewModel.onSearchSensorsClick() },
        modifier = Modifier.padding(vertical = Padding.x_small),
//        enabled = state.program.isNotEmpty()
    ) {
        Text(text = stringResource(id = R.string.compose_workout_info_search_sensors))
    }

    LoadingButton(
        onClick = { viewModel.onRunWorkoutClick() },
        modifier = Modifier.padding(vertical = Padding.x_small),
//        enabled = state.program.isNotEmpty()
    ) {
        Text(text = stringResource(id = R.string.compose_workout_info_run))
    }

    LoadingButton(
        onClick = { viewModel.onDeleteClick() },
        modifier = Modifier.padding(vertical = Padding.x_small)
    ) {
        Text(text = stringResource(id = R.string.compose_workout_info_delete))
    }
}
