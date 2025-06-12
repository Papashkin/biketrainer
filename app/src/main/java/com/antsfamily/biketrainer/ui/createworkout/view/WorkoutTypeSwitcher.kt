package com.antsfamily.biketrainer.ui.createworkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.ui.createworkout.model.WorkoutType
import com.antsfamily.biketrainer.ui.createworkout.model.getIconId
import com.antsfamily.biketrainer.ui.util.Padding

@Composable
fun WorkoutTypeSwitcher(
    modifier: Modifier = Modifier,
    onTypeClick: (WorkoutType) -> Unit
) {
    LazyRow(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(WorkoutType.entries) { workout ->
            WorkoutChip(workout) { onTypeClick(it) }
        }
    }
}

@Composable
fun WorkoutChip(
    type: WorkoutType,
    modifier: Modifier = Modifier,
    onClick: (WorkoutType) -> Unit
) {
    Box(
        modifier
            .size(width = 80.dp, height = 64.dp)
            .clickable { onClick(type) }
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                RoundedCornerShape(6.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(type.getIconId()),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                contentDescription = null)
            Text(
                type.name,
                Modifier.padding(top = Padding.regular),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun WorkoutTypeSwitcher2Preview() {
    WorkoutTypeSwitcher {}
}
