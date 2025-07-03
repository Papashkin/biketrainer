package com.antsfamily.biketrainer.ui.workoutinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.core.view.TopBar
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutStepCard
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.domain.model.Duration
import com.antsfamily.domain.model.IndexedWorkoutStep
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.model.WorkoutStep

@Composable
fun WorkoutInfoContentScreen(
    modifier: Modifier = Modifier,
    onNavigationBackClick: () -> Unit,
    onSearchSensorsClick: () -> Unit,
    onRunWorkoutClick: () -> Unit,
    onDeleteClick: (Int) -> Unit,
    workout: Workout
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
            .navigationBarsPadding()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            TopBar(
                onNavigationBack = { onNavigationBackClick() }
            )
            Column(modifier = Modifier.padding(horizontal = Padding.medium)) {
                Text(
                    text = workout.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Padding.small)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Padding.medium),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        R.string.compose_workout_info_description,
                        workout.totalDuration.fullTimeFormat(),
                        workout.averagePower,
                        workout.maxPower
                    ),
                    modifier = Modifier
                        .padding(vertical = Padding.regular)
                        .weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = Padding.medium, vertical = Padding.small)
            )

            Text(
                text = "Workout steps",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = Padding.medium, vertical = Padding.small)
            )

            LazyColumn {
                items(workout.data) { data ->
                    WorkoutStepCard(step = data.step)
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = Padding.medium)
                .navigationBarsPadding()
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            Column {
                LoadingButton(
                    onClick = { onRunWorkoutClick() },
                    modifier = Modifier.padding(vertical = Padding.x_small),
//                enabled = state.program.isNotEmpty()
                ) {
                    Text(text = stringResource(id = R.string.compose_workout_info_run))
                }

                LoadingButton(
                    onClick = { onDeleteClick(workout.id) },
                    modifier = Modifier.padding(vertical = Padding.x_small)
                ) {
                    Text(text = stringResource(id = R.string.compose_workout_info_delete))
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun WorkoutInfoContentScreenPreview() {
    WorkoutInfoContentScreen(
        workout = Workout(
            1, "mock 1", listOf(
                IndexedWorkoutStep(
                    1, WorkoutStep.OneStep(200, Duration(12, 35))
                ),
                IndexedWorkoutStep(
                    2, WorkoutStep.Intervals(1200, Duration(1, 0), 300, Duration(1, 40), 6)
                ),
                IndexedWorkoutStep(
                    3, WorkoutStep.WarmUp(100, 240, Duration(3, 55))
                ),
                IndexedWorkoutStep(
                    4, WorkoutStep.CoolDown(400, 130, Duration(1, 30))
                )
            )
        ),
        onNavigationBackClick = {},
        onRunWorkoutClick = {},
        onDeleteClick = {},
        onSearchSensorsClick = {}
    )
}