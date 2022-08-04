package com.antsfamily.biketrainer.ui.createworkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutState
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutViewModel2
import com.antsfamily.biketrainer.ui.common.*
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.textColor
import com.antsfamily.biketrainer.util.STRING_EMPTY
import com.antsfamily.domain.antservice.orFalse
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

    var name by rememberSaveable { mutableStateOf("") }
    var power by rememberSaveable { mutableStateOf(0) }
    var duration by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearFieldsEvent.collect {
            power = 0
            duration = ""
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
                "New workout",
                fontSize = FontSize.H4,
                style = TextStyle(color = textColor),
                modifier = Modifier.padding(top = Padding.large)
            )

            when (val data = uiState.value) {
                CreateWorkoutState.Loading -> FullScreenLoading()
                is CreateWorkoutState.Workouts -> {
                    if (data.steps.isEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200f.dp)
                                .padding(top = Padding.large)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = Padding.large)
                                ) {
                                    Text(text = "There is nothing to show yet")
                                }
                            }
                        }
                    } else {
                        WorkoutChart(
                            modifier = Modifier.padding(top = Padding.large),
                            workoutSteps = data.steps
                        )
                    }
                }
                else -> {
                    /* no-op */
                }
            }

            OutlinedTextFieldWithErrorState(
                label = "Power, W",
                value = if (power != 0) power.toString() else STRING_EMPTY,
                onValueChange = {
                    power = it.toIntOrNull().orZero()
                    viewModel.onPowerChanged()
                },
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .padding(top = Padding.x_large)
                    .fillMaxSize()
            )

            DurationOutlinedTextField(
                label = "Duration",
                onValueChange = {
                    duration = it
                    viewModel.onDurationChanged()
                },
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Padding.regular)
            )

            OutlinedTextFieldWithErrorState(
                label = "Workout name",
                value = name,
                onValueChange = {
                    name = it
                    viewModel.onWorkoutNameChanged()
                },
                errorMessage = (uiState.value as? CreateWorkoutState.TextFieldsState)?.workoutNameError,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                modifier = Modifier
                    .padding(top = Padding.regular)
                    .fillMaxSize()
            )

            LoadingButton(
                onClick = { viewModel.onAddStepClick(power, duration) },
                modifier = Modifier.padding(top = Padding.large, bottom = Padding.x_small)
            ) {
                Text(text = "Add step")
            }

            LoadingButton(
                onClick = { viewModel.onRemoveLastStepClick() },
                modifier = Modifier.padding(vertical = Padding.x_small),
                enabled = (uiState.value as? CreateWorkoutState.Workouts)?.steps?.isNotEmpty()
                    .orFalse()
            ) {
                Text(text = "Remove last step")
            }

            LoadingButton(
                onClick = { viewModel.onSaveClick(name) },
                modifier = Modifier.padding(vertical = Padding.x_small),
                enabled = (uiState.value as? CreateWorkoutState.Workouts)?.steps?.isNotEmpty()
                    .orFalse()
            ) {
                Text(text = "Save")
            }
        }
    }
}
