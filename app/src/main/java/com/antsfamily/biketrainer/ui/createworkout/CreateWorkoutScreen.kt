package com.antsfamily.biketrainer.ui.createworkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutState
import com.antsfamily.biketrainer.presentation.createworkout.CreateWorkoutViewModel2
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.common.WorkoutChart
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.textColor
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
                value = if (power != 0) power.toString() else STRING_EMPTY,
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
                label = { Text("Duration") },
                value = if (duration != 0L) duration.toString() else STRING_EMPTY,
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
                    .size(height = 0.dp, width = 100.dp)
                    .alpha(0f)
                    .padding(top = Padding.medium)
            )
            DurationView(
                modifier = Modifier.padding(top = Padding.regular),
                title = "Enter duration, sec.",
                value = duration
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
                    .padding(top = Padding.medium)
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
                onClick = { viewModel.onAddStepClick(power, duration) },
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
                onClick = { viewModel.onSaveClick(name) },
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

@Composable
fun DurationView(
    modifier: Modifier,
    title: String,
    value: Long,
) {
    val duration = ZEROS.plus(value).takeLast(6)
//        OutlinedTextField(
//            label = {},
//            value = "",
//            onValueChange = { onValueChange.invoke(duration) },
//            singleLine = singleLine,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Number,
//                imeAction = ImeAction.Done
//            ),
//            modifier = Modifier.size(1.dp).alpha(0f)
//        )
    Column(modifier = modifier) {
        Text(text = title, modifier = Modifier.padding(start = Padding.regular))
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = Padding.x_small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = duration.take(2),
                fontSize = FontSize.Body1,
                modifier = Modifier.padding(start = Padding.large, end = Padding.x_small)
            )
            Text(
                text = "h",
                fontSize = FontSize.Body2,
                modifier = Modifier.padding(horizontal = Padding.x_small)
            )
            Text(
                text = " : ",
                fontSize = FontSize.Body1,
                modifier = Modifier.padding(horizontal = Padding.x_small)
            )
            Text(
                text = duration.take(4).takeLast(2),
                fontSize = FontSize.Body1,
                modifier = Modifier.padding(horizontal = Padding.x_small)
            )
            Text(
                text = "m",
                fontSize = FontSize.Body2,
                modifier = Modifier.padding(horizontal = Padding.x_small)
            )
            Text(
                text = " : ",
                fontSize = FontSize.Body1,
                modifier = Modifier.padding(horizontal = Padding.x_small)
            )
            Text(
                text = duration.takeLast(2),
                fontSize = FontSize.Body1,
                modifier = Modifier.padding(horizontal = Padding.x_small)
            )
            Text(
                text = "s",
                fontSize = FontSize.Body2,
                modifier = Modifier.padding(start = Padding.x_small, end = Padding.large)
            )

        }
    }
}

const val ZEROS = "000000"
