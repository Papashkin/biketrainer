package com.antsfamily.biketrainer.ui.createworkout.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.getIconId
import com.antsfamily.biketrainer.ui.util.getWorkoutSubtitle
import com.antsfamily.biketrainer.ui.util.getWorkoutTitle
import com.antsfamily.domain.model.Duration
import com.antsfamily.domain.model.WorkoutStep

@Composable
fun WorkoutStepCard(
    step: WorkoutStep,
    onDeleteClick: (() -> Unit)? = null,
    onEditClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    ListItem(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surfaceContainer, RectangleShape),
        headlineContent = { Text(context.getWorkoutTitle(step)) },
        supportingContent = { Text(context.getWorkoutSubtitle(step)) },
        leadingContent = {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(Padding.small)
            ) {
                Icon(ImageVector.vectorResource(step.getIconId()), null)
            }
        },
        trailingContent = {
            Row {
                onEditClick?.let {
                    Icon(
                        Icons.Outlined.Edit,
                        null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.clickable { it.invoke() }
                    )
                }
                Spacer(Modifier.width(10.dp))
                onDeleteClick?.let {

                    Icon(
                        Icons.Outlined.Delete,
                        null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.clickable { it.invoke() }
                    )
                }
            }
        },
    )
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(start = Padding.medium),
        color = MaterialTheme.colorScheme.surfaceDim
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WorkoutStepCardPreview() {
    val duration = Duration(2, 55)
    Column {
        WorkoutStepCard(
            step = WorkoutStep.OneStep(200, duration),
            onDeleteClick = {},
            onEditClick = {}
        )
        WorkoutStepCard(
            step = WorkoutStep.Intervals(1200, duration, 300, duration, 6),
            onDeleteClick = {},
            onEditClick = {})
        WorkoutStepCard(
            step = WorkoutStep.WarmUp(100, 240, duration),
            onDeleteClick = {},
            onEditClick = {})
        WorkoutStepCard(
            step = WorkoutStep.CoolDown(400, 130, duration),
        )
    }
}