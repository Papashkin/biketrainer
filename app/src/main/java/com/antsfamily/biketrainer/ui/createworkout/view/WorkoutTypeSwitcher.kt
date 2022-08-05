package com.antsfamily.biketrainer.ui.createworkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding

@Composable
fun WorkoutTypeSwitcher(
    modifier: Modifier,
    type: WorkoutType = WorkoutType.STEP,
    onTypeChanged: ((WorkoutType) -> Unit) = {}
) {
    var workoutType by rememberSaveable { mutableStateOf(type) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface, RoundedCornerShape(10.dp))
    ) {
        ClickableText(
            text = AnnotatedString(
                text = "Step",
                spanStyle = SpanStyle(
                    color = when (workoutType) {
                        WorkoutType.STEP -> MaterialTheme.colors.onPrimary
                        WorkoutType.INTERVAL -> MaterialTheme.colors.onSurface
                    },
                    fontSize = FontSize.Body1
                ),
            ),
            style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier
                .padding(Padding.x_small)
                .alpha {
                    when (workoutType) {
                        WorkoutType.STEP -> 1f
                        WorkoutType.INTERVAL -> 0.4f
                    }
                }
                .wrapContentHeight(align = Alignment.CenterVertically)
                .weight(1f)
                .background(
                    when (workoutType) {
                        WorkoutType.STEP -> MaterialTheme.colors.primaryVariant
                        WorkoutType.INTERVAL -> MaterialTheme.colors.surface
                    }, RoundedCornerShape(8.dp)
                ),
            onClick = {
                workoutType = WorkoutType.STEP
                onTypeChanged.invoke(workoutType)
            }
        )
        ClickableText(
            text = AnnotatedString(
                text = "Interval",
                spanStyle = SpanStyle(
                    color = when (workoutType) {
                        WorkoutType.STEP -> MaterialTheme.colors.onSurface
                        WorkoutType.INTERVAL -> MaterialTheme.colors.onPrimary
                    },
                    fontSize = FontSize.Body1,
                )
            ),
            style = TextStyle(textAlign = TextAlign.Center),
            modifier = Modifier
                .padding(Padding.x_small)
                .alpha {
                    when (workoutType) {
                        WorkoutType.INTERVAL -> 1f
                        WorkoutType.STEP -> 0.4f
                    }
                }
                .wrapContentHeight(align = Alignment.CenterVertically)
                .weight(1f)
                .background(
                    when (workoutType) {
                        WorkoutType.INTERVAL -> MaterialTheme.colors.primaryVariant
                        WorkoutType.STEP -> MaterialTheme.colors.surface
                    }, RoundedCornerShape(8.dp)
                ),
            onClick = {
                workoutType = WorkoutType.INTERVAL
                onTypeChanged.invoke(workoutType)
            }
        )
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
