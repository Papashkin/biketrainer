package com.antsfamily.biketrainer.ui.createworkout.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.ui.createworkout.model.Duration
import com.antsfamily.biketrainer.ui.util.Padding

@Composable
fun DurationView(
    duration: Duration = Duration.Empty,
    onValueChanged: (Duration) -> Unit
) {
    val (newDuration, setNewDuration) = remember { mutableStateOf(duration) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Padding.regular, vertical = Padding.tiny),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                Padding.regular,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SwipeableCounter(
                width = 32.dp,
                itemHeight = 32.dp,
                items = DURATION_LIST_MINUTES,
                initialItem = duration.minutes
            ) { item ->
                setNewDuration(duration.copy(minutes = item))
            }
            Text(":")
            SwipeableCounter(
                width = 32.dp,
                itemHeight = 32.dp,
                items = DURATION_LIST_SECONDS,
                initialItem = duration.seconds
            ) { item ->
                setNewDuration(duration.copy(seconds = item))
            }
        }
    }

    LaunchedEffect(newDuration) {
        onValueChanged(newDuration)
    }
}

private val DURATION_LIST_SECONDS = (0..59 step 5).toList()
private val DURATION_LIST_MINUTES = (0..59).toList()


@Preview
@Composable
private fun DurationViewPreview() {
    DurationView(Duration(2, 55), { })
}