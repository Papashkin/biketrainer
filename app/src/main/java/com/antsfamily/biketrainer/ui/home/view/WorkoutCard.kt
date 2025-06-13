package com.antsfamily.biketrainer.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.common.IconCardRounded
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.domain.model.Duration
import com.antsfamily.domain.model.IndexedWorkoutStep
import com.antsfamily.domain.model.Workout
import com.antsfamily.domain.model.WorkoutStep

@Composable
fun WorkoutCard(
    workout: Workout,
    onWorkoutClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = { onWorkoutClick(workout.id) }
    ) {
        ListItem(
            modifier = Modifier.height(100.dp),
            headlineContent = {
                Text(
                    text = workout.title,
                    style = MaterialTheme.typography.titleSmall,
                )
            },
            supportingContent = {
                    Text(
                        text = stringResource(
                            id = R.string.compose_home_workout_support_text,
                            workout.averagePower,
                            workout.totalDuration.fullTimeFormat()
                        ),
                        style = MaterialTheme.typography.bodySmall,
                    )
            },
            leadingContent = {
                IconCardRounded(imageRes = R.drawable.ic_workout)
            },
            trailingContent = {
                Icon(
                    Icons.Outlined.Edit,
                    null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onEditClick(workout.id) }
                )
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WorkoutCardPreview() {
    WorkoutCard(
        Workout(
            id = 2,
            title = "test 1",
            data = listOf(
                IndexedWorkoutStep(1, WorkoutStep.OneStep(200, Duration(1, 0)))
            )
        ), {}, {})
}