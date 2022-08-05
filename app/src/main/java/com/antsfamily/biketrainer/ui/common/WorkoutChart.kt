package com.antsfamily.biketrainer.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.primaryVariantColor
import com.antsfamily.data.model.program.ProgramData

private const val WORKOUT_VIEW_HEIGHT = 200f
private const val WORKOUT_VIEW_START_X_AXIS = 0f

@Composable
fun WorkoutChart(
    modifier: Modifier = Modifier,
    width: Float? = null,
    height: Float = WORKOUT_VIEW_HEIGHT,
    workoutSteps: List<ProgramData>,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    var startXaxis = WORKOUT_VIEW_START_X_AXIS
    val updatedModifier = if (width != null) {
        modifier.size(
            height = height.dp,
            width = width.dp
        )
    } else {
        modifier
            .fillMaxWidth()
            .height(height.dp)
    }
    Card(
        modifier = updatedModifier,
        shape = shape
    ) {
        if (workoutSteps.isEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Padding.large)
                ) {
                    Text(text = stringResource(id = R.string.compose_create_workout_empty_chart))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(Padding.x_small)
                    .wrapContentSize(align = Alignment.BottomStart)
            ) {
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    val allStepsWidth = workoutSteps.sumOf { it.duration }
                    val stepWidthCoefficient = size.width / allStepsWidth
                    workoutSteps.forEach { step ->
                        val durationWithCoef = stepWidthCoefficient * step.duration.toFloat()
                        drawRect(
                            primaryVariantColor,
                            topLeft = Offset(startXaxis, 0.0f),
                            size = Size(durationWithCoef, -step.power.toFloat())
                        )
                        startXaxis += durationWithCoef
                    }
                }
            }
        }
    }
}
