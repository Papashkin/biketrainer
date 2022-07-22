package com.antsfamily.biketrainer.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.secondaryColor
import com.antsfamily.data.model.program.ProgramData

@Composable
fun WorkoutChart(
    modifier: Modifier = Modifier,
    width: Float? = null,
    height: Float? = null,
    workoutSteps: List<ProgramData>
) {
    if (workoutSteps.isEmpty()) return
    var startXaxis = 0.0f
    val viewHeight = height ?: workoutSteps.maxOf { it.power }.toFloat()
    val updatedModifier = if (width != null) {
        modifier.size(
            height = viewHeight.dp,
            width = width.dp
        )
    } else {
        modifier
            .fillMaxWidth()
            .height(viewHeight.dp)
    }
    Card(
        modifier = updatedModifier
//        modifier = modifier
//            .fillMaxWidth()
//            .height(
//                workoutSteps.maxOf { it.power }.dp
//            )
    ) {
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
                        secondaryColor,
                        topLeft = Offset(startXaxis, 0.0f),
                        size = Size(durationWithCoef, -step.power.toFloat())
                    )
                    startXaxis += durationWithCoef
                }
            }
        }
    }
}
