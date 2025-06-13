package com.antsfamily.biketrainer.ui.createworkout.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.presentation.createworkout.WorkoutOneStepDialogViewModel
import com.antsfamily.biketrainer.ui.common.TextFieldWithErrorState
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.util.orEmpty
import com.antsfamily.domain.model.Duration
import com.antsfamily.domain.model.WorkoutStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutOneStepDialog(
    step: WorkoutStep.OneStep? = null,
    onDismiss: () -> Unit,
    onConfirmClick: (WorkoutStep.OneStep) -> Unit,
    viewModel: WorkoutOneStepDialogViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateBackEvent.collect {
            onConfirmClick(it)
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = SheetState(
            density = Density(LocalContext.current),
            skipPartiallyExpanded = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = Padding.medium),
            verticalArrangement = Arrangement.spacedBy(Padding.small),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = step?.let { "Update existed One-Step stage" } ?: "Add a One-Step stage",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            TextFieldWithErrorState(
                value = state.value.power?.toString().orEmpty(),
                label = "Power, W",
                onValueChange = { viewModel.onPowerChanged(it.toIntOrNull()) },
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number,
                errorMessage = state.value.power?.let {
                    if (it <= 50) {
                        "Power should be more than 50"
                    } else null
                }
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Swipe up and down to set duration up",
                    style = MaterialTheme.typography.labelMedium
                )
                DurationView(state.value.duration) { viewModel.onDurationChanged(it) }
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
                    onClick = { viewModel.onConfirmClick() },
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
private fun UsernameChangeDialogPreview1() {
    WorkoutOneStepDialog(null, {}, {})
}

@Preview
@Composable
private fun UsernameChangeDialogPreview2() {
    WorkoutOneStepDialog(WorkoutStep.OneStep(120, Duration(1, 0)), {}, {})
}