package com.antsfamily.biketrainer.ui.createworkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutState
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutViewModel2
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.common.WorkoutChart
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.textColor
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

    val isWorkoutNameError =
        (uiState.value as? CreateWorkoutState.TextFieldsState)?.workoutNameError != null

    var name by rememberSaveable { mutableStateOf("") }
    var power by rememberSaveable { mutableStateOf(0) }
    var duration by rememberSaveable { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        viewModel.clearFieldsEvent.collect {
            power = 0
            duration = 0
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
            IconButton(
                onClick = { navController.popBackStack() },
                Modifier.padding(top = Padding.large)
            ) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
            Text(
                "Workout creation",
                fontSize = FontSize.H4,
                style = TextStyle(color = textColor),
                modifier = Modifier.padding(top = Padding.large)
            )

            when (val data = uiState.value) {
                CreateWorkoutState.Loading -> {
                    Card(modifier = Modifier.padding(vertical = Padding.huge)) {
                        Text(text = "There is no steps yet")
                    }
                }
                is CreateWorkoutState.Workouts -> WorkoutChart(
                    modifier = Modifier.padding(top = Padding.large),
                    workoutSteps = data.steps
                )
                else -> {
                    /* no-op */
                }
            }

            OutlinedTextField(
                label = { Text("Enter power, W") },
                value = power.toString(),
                onValueChange = {
                    power = it.toIntOrNull().orZero()
                    viewModel.onPowerChanged()
                },
                isError = false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .padding(top = Padding.x_large)
                    .fillMaxSize()
            )

            OutlinedTextField(
                label = { Text("Enter duration, sec.") },
                value = duration.toString(),
                onValueChange = {
                    duration = it.toLongOrNull().orZero()
                    viewModel.onDurationChanged()
                },
                isError = false,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .padding(top = Padding.x_large)
                    .fillMaxSize()
            )

            OutlinedTextField(
                label = { Text("Enter the workout name") },
                value = name,
                onValueChange = {
                    name = it
                    viewModel.onWorkoutNameChanged()
                },
                isError = isWorkoutNameError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .padding(top = Padding.x_large)
                    .fillMaxSize()
            )
            if (isWorkoutNameError) {
                Text(
                    text = (uiState.value as? CreateWorkoutState.TextFieldsState)?.workoutNameError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = Padding.medium)
                )
            }

            LoadingButton(
                onClick = { viewModel.onAddStepClick(name, power, duration) },
                modifier = Modifier.padding(top = Padding.large, bottom = Padding.small)
            ) {
                Text(text = "Add step")
            }

            LoadingButton(
                onClick = { viewModel.onRemoveLastStepClick() },
                modifier = Modifier.padding(vertical = Padding.small),
                enabled = (uiState.value as? CreateWorkoutState.Workouts)?.steps.orEmpty()
                    .isNotEmpty()
            ) {
                Text(text = "Remove last step")
            }

            LoadingButton(
                onClick = { viewModel.onSaveClick() },
                modifier = Modifier.padding(vertical = Padding.small),
                enabled = (uiState.value as? CreateWorkoutState.Workouts)?.steps.orEmpty()
                    .isNotEmpty()
            ) {
                Text(text = "Save")
            }

            LoadingButton(
                onClick = { viewModel.onCancelClick() },
                modifier = Modifier.padding(vertical = Padding.small)
            ) {
                Text(text = "Cancel")
            }

        }
    }
}
