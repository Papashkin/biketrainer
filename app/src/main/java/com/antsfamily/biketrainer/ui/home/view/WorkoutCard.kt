package com.antsfamily.biketrainer.ui.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.common.workoutchart.WorkoutChart
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.TextStyles
import com.antsfamily.data.model.program.Program

private const val WORKOUT_CHART_SIZE = 200f

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutCard(
    workout: Program,
    onWorkoutClick: (Program) -> Unit
) {
    Card(
        modifier = Modifier.padding(start = Padding.tiny, end = Padding.small),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        onClick = { onWorkoutClick(workout) }
    ) {
        Column {
            WorkoutChart(
                width = WORKOUT_CHART_SIZE,
                height = WORKOUT_CHART_SIZE,
                workoutSteps = workout.data
            )
            Column {
                Text(
                    text = workout.title,
                    style = TextStyle(fontSize = FontSize.H5, fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(Padding.tiny)
                )
                Text(
                    text = stringResource(
                        id = R.string.compose_home_workout_power,
                        workout.getAveragePower()
                    ),
                    style = TextStyles.body1(),
                    modifier = Modifier.padding(start = Padding.tiny, top = Padding.small)
                )
                Text(
                    text = workout.getDuration(),
                    fontSize = FontSize.Caption,
                    modifier = Modifier.padding(Padding.tiny)
                )
            }
        }
    }
}
