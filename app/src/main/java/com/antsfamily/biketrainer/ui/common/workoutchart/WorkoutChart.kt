package com.antsfamily.biketrainer.ui.common.workoutchart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.primaryVariantColor
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.data.model.program.getTotalDuration

private const val WORKOUT_VIEW_HEIGHT = 200f

@Composable
fun WorkoutChart(
    modifier: Modifier = Modifier,
    width: Float? = null,
    height: Float = WORKOUT_VIEW_HEIGHT,
    workoutSteps: List<ProgramData>,
    isTextVisible: Boolean = false,
) {
    val state = rememberSaveable(saver = WorkoutChartState.Saver) {
        WorkoutChartState.getState(workoutSteps)
    }

    var startXaxis: Float
    val updatedModifier = if (width != null) {
        modifier.size(height = height.dp, width = width.dp)
    } else {
        modifier
            .fillMaxWidth()
            .height(height.dp)
    }

    BoxWithConstraints(
        modifier = updatedModifier
            .padding(top = Padding.small)
            .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(2.dp))
            .wrapContentSize(align = Alignment.BottomStart)
    ) {
        state.update(workoutSteps)
        val viewMaxHeight = constraints.maxHeight.minus(Padding.regular.value)
        val viewMaxWidth = constraints.maxWidth.minus(Padding.regular.value)
        val textPaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = FontSize.H5.value
            color = MaterialTheme.colors.primaryVariant.toArgb()
        }

        Canvas(modifier = Modifier) {
            val stepWidthCoefficient = viewMaxWidth / workoutSteps.getTotalDuration()

            if (isTextVisible) {

                // Vertical (Y) axis
                drawLine(
                    color = Color.LightGray,
                    strokeWidth = Padding.x_small.value,
                    start = Offset(0f, 0f),
                    end = Offset(0f, viewMaxHeight.unaryMinus())
                )

                // Horizontal (X) axis
                drawLine(
                    color = Color.LightGray,
                    strokeWidth = Padding.x_small.value,
                    start = Offset(0f, 0f),
                    end = Offset(viewMaxWidth, 0f)
                )

                state.powerLines.forEach { value ->
                    if (value > viewMaxHeight) return@forEach
                        drawLine(
                            color = Color.LightGray,
                            strokeWidth = Padding.tiny.value,
                            start = Offset(0f, -value.toFloat()),
                            end = Offset(viewMaxWidth, -value.toFloat()),
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(10f, 20f),
                                phase = 5f
                            )
                        )
                        drawIntoCanvas {
                            it.nativeCanvas.drawText(
                                value.toString(),
                                Padding.x_large.value.unaryMinus(),
                                value.toFloat().minus(10).unaryMinus(),
                                textPaint
                            )
                    }
                }

                state.durationLines.forEach { (value, label) ->
                    val valueWithCoef = stepWidthCoefficient * value
                    drawLine(
                        color = Color.LightGray,
                        strokeWidth = Padding.tiny.value,
                        start = Offset(valueWithCoef, Padding.tiny.value),
                        end = Offset(valueWithCoef, viewMaxHeight.unaryMinus()),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(10f, 20f),
                            phase = 5f
                        )
                    )
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            label,
                            valueWithCoef,
                            Padding.xx_large.value,
                            textPaint
                        )
                    }
                }
            }
            startXaxis = 0f
            workoutSteps.forEach { step ->
                val endYcoord = -step.power.div(state.getPowerCoef(viewMaxHeight))
                val durationWithCoef = state.getDurationCoef(viewMaxWidth).times(step.duration)

                if (isTextVisible && state.isLabelReadable()) {
                    drawIntoCanvas {
                        it.nativeCanvas.drawText(
                            step.power.toString(),
                            startXaxis + durationWithCoef.div(2),
                            endYcoord.minus(10),
                            textPaint
                        )
                    }
                }
                drawRect(
                    primaryVariantColor,
                    topLeft = Offset(startXaxis, 0.0f),
                    size = Size(durationWithCoef, endYcoord)
                )
                startXaxis += durationWithCoef
            }
        }
    }
}
