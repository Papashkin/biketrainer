package com.antsfamily.biketrainer.ui.createworkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutState
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutViewModel2
import com.antsfamily.biketrainer.presentation.createworkout.WorkoutItem
import com.antsfamily.biketrainer.ui.common.*
import com.antsfamily.biketrainer.ui.common.workoutchart.WorkoutChart
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutType
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutTypeSwitcher
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.util.STRING_EMPTY
import com.antsfamily.domain.antservice.orZero

interface CreateWorkoutScreen {
    companion object {
        @Composable
        fun Content(navController: NavController) {
            CreateWorkoutScreen(navController)
        }
    }
}

@Composable
fun CreateWorkoutScreen(
    navController: NavController,
    viewModel: CreateWorkoutViewModel2 = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    var workoutItem by rememberSaveable {
        mutableStateOf(WorkoutItem())
    }
    var name by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearFieldsEvent.collect {
            name = ""
            workoutItem = WorkoutItem()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateBackEvent.collect {
            navController.popBackStack()
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Padding.large)
        ) {
            Text(
                text = stringResource(id = R.string.compose_create_workout_title),
                fontSize = FontSize.H4,
                modifier = Modifier.padding(top = Padding.large)
            )

            if (uiState.value.isLoading) {
                FullScreenLoading()
            } else {
                WorkoutContentView(
                    uiState.value,
                    viewModel,
                    name,
                    workoutItem,
                    onNameChanged = { name = it },
                    onPowerChanged = { workoutItem = workoutItem.copy(power = it) },
                    onDurationChanged = { workoutItem = workoutItem.copy(duration = it) },
                    onPowerRestChanged = { workoutItem = workoutItem.copy(powerRest = it) },
                    onDurationRestChanged = { workoutItem = workoutItem.copy(durationRest = it) },
                    onRepeatsChanged = { workoutItem = workoutItem.copy(repeats = it) },
                )
            }
        }
    }
}

@Composable
fun WorkoutContentView(
    state: CreateWorkoutState,
    viewModel: CreateWorkoutViewModel2,
    name: String,
    workoutItem: WorkoutItem,
    onNameChanged: (String) -> Unit,
    onDurationChanged: (String) -> Unit,
    onPowerChanged: (Int) -> Unit,
    onDurationRestChanged: (String) -> Unit,
    onPowerRestChanged: (Int) -> Unit,
    onRepeatsChanged: (Int) -> Unit,
) {
    WorkoutChart(
        modifier = Modifier.padding(top = Padding.small),
        workoutSteps = state.steps,
        isTextVisible = true
    )
    OutlinedTextFieldWithErrorState(
        label = stringResource(id = R.string.compose_create_workout_name),
        value = name,
        onValueChange = {
            onNameChanged(it)
            viewModel.onWorkoutNameChanged()
        },
        errorMessage = state.nameError,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done,
        modifier = Modifier
            .padding(top = Padding.small)
            .fillMaxSize()
    )
    WorkoutTypeSwitcher(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Padding.regular)
    ) {
        viewModel.onWorkoutTypeChanged(it)
    }
    when (state.workoutType) {
        WorkoutType.STEP -> {
            StepContentView(
                viewModel,
                state,
                workoutItem,
                onDurationChanged,
                onPowerChanged
            )
            LoadingButton(
                onClick = { viewModel.onAddStepClick(workoutItem) },
                modifier = Modifier.padding(top = Padding.regular, bottom = Padding.x_small)
            ) {
                Text(text = stringResource(id = R.string.compose_create_workout_add_step))
            }
        }
        WorkoutType.INTERVAL -> {
            IntervalContentView(
                viewModel,
                state,
                workoutItem,
                onDurationChanged,
                onPowerChanged,
                onDurationRestChanged,
                onPowerRestChanged,
                onRepeatsChanged
            )
            LoadingButton(
                onClick = { viewModel.onAddIntervalClick(workoutItem) },
                modifier = Modifier.padding(top = Padding.regular, bottom = Padding.x_small)
            ) {
                Text(text = stringResource(id = R.string.compose_create_workout_add_interval))
            }
        }
    }

    LoadingButton(
        onClick = { viewModel.onRemoveLastStepClick() },
        modifier = Modifier.padding(vertical = Padding.x_small),
        enabled = state.steps.isNotEmpty()
    ) {
        Text(text = stringResource(id = R.string.compose_create_workout_remove_last_step))
    }
    LoadingButton(
        onClick = { viewModel.onSaveClick(name) },
        modifier = Modifier.padding(vertical = Padding.x_small),
        enabled = state.steps.isNotEmpty()
    ) {
        Text(text = stringResource(id = R.string.compose_create_workout_save))
    }
}

@Composable
fun StepContentView(
    viewModel: CreateWorkoutViewModel2,
    state: CreateWorkoutState,
    workoutItem: WorkoutItem,
    onDurationChanged: (String) -> Unit,
    onPowerChanged: (Int) -> Unit,
) {
    OutlinedTextFieldWithErrorState(
        label = stringResource(id = R.string.compose_create_workout_power),
        value = if (workoutItem.power != 0) workoutItem.power.toString() else STRING_EMPTY,
        onValueChange = {
            onPowerChanged(it.toIntOrNull().orZero())
            viewModel.onPowerChanged()
        },
        keyboardType = KeyboardType.Number,
        errorMessage = state.powerError,
        imeAction = ImeAction.Next,
        modifier = Modifier.padding(top = Padding.small)
    )

    DurationOutlinedTextField(
        label = stringResource(id = R.string.compose_create_workout_duration),
        onValueChange = {
            onDurationChanged(it)
            viewModel.onDurationChanged()
        },
        keyboardType = KeyboardType.Number,
        errorMessage = state.durationError,
        imeAction = ImeAction.Next,
        modifier = Modifier.padding(top = Padding.small)
    )
}

@Composable
fun IntervalContentView(
    viewModel: CreateWorkoutViewModel2,
    state: CreateWorkoutState,
    workoutItem: WorkoutItem,
    onDurationChanged: (String) -> Unit,
    onPowerChanged: (Int) -> Unit,
    onDurationRestChanged: (String) -> Unit,
    onPowerRestChanged: (Int) -> Unit,
    onRepeatsChanged: (Int) -> Unit,
) {
    Row {
        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextFieldWithErrorState(
                label = stringResource(id = R.string.compose_create_workout_power),
                value = if (workoutItem.power != 0) workoutItem.power.toString() else STRING_EMPTY,
                onValueChange = {
                    onPowerChanged(it.toIntOrNull().orZero())
                    viewModel.onPowerChanged()
                },
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                errorMessage = state.powerError,
                modifier = Modifier.padding(top = Padding.small)
            )
            DurationOutlinedTextField(
                label = stringResource(id = R.string.compose_create_workout_duration),
                onValueChange = {
                    onDurationChanged(it)
                    viewModel.onDurationChanged()
                },
                keyboardType = KeyboardType.Number,
                errorMessage = state.durationError,
                imeAction = ImeAction.Next,
                modifier = Modifier.padding(top = Padding.small)
            )
        }
        Spacer(modifier = Modifier.width(Padding.small))
        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextFieldWithErrorState(
                label = stringResource(id = R.string.compose_create_workout_power_rest),
                value = if (workoutItem.powerRest != 0) workoutItem.powerRest.toString() else STRING_EMPTY,
                onValueChange = {
                    onPowerRestChanged(it.toIntOrNull().orZero())
                    viewModel.onPowerRestChanged()
                },
                keyboardType = KeyboardType.Number,
                errorMessage = state.powerRestError,
                imeAction = ImeAction.Next,
                modifier = Modifier.padding(top = Padding.small)
            )
            DurationOutlinedTextField(
                label = stringResource(id = R.string.compose_create_workout_duration_rest),
                onValueChange = {
                    onDurationRestChanged(it)
                    viewModel.onDurationRestChanged()
                },
                keyboardType = KeyboardType.Number,
                errorMessage = state.durationRestError,
                imeAction = ImeAction.Next,
                modifier = Modifier.padding(top = Padding.small)
            )
        }
    }
    OutlinedTextFieldWithErrorState(
        label = stringResource(id = R.string.compose_create_workout_repeats),
        value = if (workoutItem.repeats != 0) workoutItem.repeats.toString() else STRING_EMPTY,
        onValueChange = {
            onRepeatsChanged(it.toIntOrNull().orZero())
            viewModel.onRepeatsChanged()
        },
        keyboardType = KeyboardType.Number,
        errorMessage = state.repeatsError,
        imeAction = ImeAction.Done,
        modifier = Modifier.padding(top = Padding.small)
    )
}
