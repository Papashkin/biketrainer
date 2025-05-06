package com.antsfamily.biketrainer.ui.createworkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.ui.util.FontSize

@Composable
fun WorkoutTypeSwitcher(
    modifier: Modifier,
    type: WorkoutType = WorkoutType.STEP,
    onTypeChanged: ((WorkoutType) -> Unit) = {}
) {
    val interactionSource = remember { MutableInteractionSource() }

    var workoutType by rememberSaveable { mutableStateOf(type) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .weight(1f)
                .background(
                    when (workoutType) {
                        WorkoutType.STEP -> MaterialTheme.colorScheme.primary
                        WorkoutType.INTERVAL -> MaterialTheme.colorScheme.surface
                    }, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                )
                .alpha {
                    when (workoutType) {
                        WorkoutType.STEP -> 1f
                        WorkoutType.INTERVAL -> 0.4f
                    }
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    workoutType = WorkoutType.STEP
                    onTypeChanged.invoke(workoutType)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Step",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = when (workoutType) {
                        WorkoutType.STEP -> MaterialTheme.colorScheme.onPrimary
                        WorkoutType.INTERVAL -> MaterialTheme.colorScheme.onSurface
                    },
                    fontSize = FontSize.Body1
                )
            )
        }
        Box(modifier = Modifier
            .height(40.dp)
            .weight(1f)
            .background(
                color = when (workoutType) {
                    WorkoutType.INTERVAL -> MaterialTheme.colorScheme.primary
                    WorkoutType.STEP -> MaterialTheme.colorScheme.surface
                }, RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
            )
            .alpha {
                when (workoutType) {
                    WorkoutType.INTERVAL -> 1f
                    WorkoutType.STEP -> 0.4f
                }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                workoutType = WorkoutType.INTERVAL
                onTypeChanged.invoke(workoutType)
            },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Interval",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = when (workoutType) {
                        WorkoutType.STEP -> MaterialTheme.colorScheme.onSurface
                        WorkoutType.INTERVAL -> MaterialTheme.colorScheme.onPrimary
                    },
                    fontSize = FontSize.Body1
                ),
            )
        }
    }
}

enum class WorkoutType {
    STEP,
    INTERVAL,
    ;
}


fun Modifier.alpha(
    action: () -> Float
) = alpha(action.invoke())
