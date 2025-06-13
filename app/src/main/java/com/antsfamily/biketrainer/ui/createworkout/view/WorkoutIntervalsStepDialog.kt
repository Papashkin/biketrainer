package com.antsfamily.biketrainer.ui.createworkout.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.createworkout.WorkoutIntervalsStepDialogViewModel
import com.antsfamily.biketrainer.ui.common.TextFieldWithErrorState
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.orEmpty
import com.antsfamily.biketrainer.util.orEmpty
import com.antsfamily.domain.model.Duration
import com.antsfamily.domain.model.WorkoutStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutIntervalsStepDialog(
    step: WorkoutStep.Intervals?,
    onDismiss: () -> Unit,
    onCloseDialogWithData: (WorkoutStep.Intervals) -> Unit,
    viewModel: WorkoutIntervalsStepDialogViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.closeDialogWithData.collect {
            onCloseDialogWithData(it)
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = SheetState(
            density = Density(LocalContext.current),
            skipPartiallyExpanded = true
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(horizontal = Padding.medium),
            verticalArrangement = Arrangement.spacedBy(Padding.small),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = step?.let { "Update existed Intervals" } ?: "Add new Intervals",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextFieldWithErrorState(
                value = state.value.workPower?.toString().orEmpty(),
                label = "Work power, W",
                onValueChange = { viewModel.onWorkPowerChanged(it.toIntOrNull()) },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number,
                errorMessage = state.value.workPower?.let {
                    if (it <= 50) {
                        "Work power should be more than 50"
                    } else null
                }
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Swipe up and down to set the work duration up",
                    style = MaterialTheme.typography.labelMedium
                )
                DurationView(state.value.workDuration.orEmpty()) {
                    viewModel.onWorkDurationChanged(it)
                }
            }
            TextFieldWithErrorState(
                value = state.value.restPower?.toString().orEmpty(),
                label = "Rest power, W",
                onValueChange = { viewModel.onRestPowerChanged(it.toIntOrNull()) },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number,
                errorMessage = state.value.restPower?.let {
                    if (it <= 50) {
                        "Rest power should be more than 50"
                    } else null
                }
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Swipe up and down to set the rest duration up",
                    style = MaterialTheme.typography.labelMedium
                )
                DurationView(state.value.restDuration.orEmpty()) {
                    viewModel.onRestDurationChanged(it)
                }
            }
            Text(text = "Amount of reps")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Padding.regular),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_minus_circle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { viewModel.onRepsDecrease() }
                )
                Text(text = state.value.reps.toString())
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_plus_circle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { viewModel.onRepsIncrease() }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.padding(Padding.small),
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(
                    onClick = {
                        viewModel.onConfirmClick()
                    },
                    enabled = state.value.isConfirmButtonEnabled,
                    modifier = Modifier.padding(Padding.small),
                ) {
                    Text(
                        text = "Confirm",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }

    LaunchedEffect(step) {
        viewModel.initialize(step)
    }
    DisposableEffect(step) {
        onDispose {
            viewModel.invalidateState()
        }
    }
}

@Preview
@Composable
private fun WorkoutIntervalsStepDialogPreview1() {
    WorkoutIntervalsStepDialog(null, {}, { })
}

@Preview
@Composable
private fun WorkoutIntervalsStepDialogPreview2() {
    WorkoutIntervalsStepDialog(
        WorkoutStep.Intervals(400, Duration(0, 20), 200, Duration(1, 0), 5),
        {},
        { })
}