package com.antsfamily.biketrainer.ui.home.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.SpanStyles
import com.antsfamily.biketrainer.ui.util.TextStyles
import com.antsfamily.data.model.program.Program

@Composable
fun HomeScreenContentWithData(
    profileName: String,
    workouts: List<Program>,
    onWorkoutClick: (Program) -> Unit,
    onCreateWorkoutClick: () -> Unit
) {
    val scrollState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = Padding.medium)) {
        HomeGreetingsView(profileName)
        Column(
            modifier = Modifier
                .padding(top = Padding.x_small)
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = stringResource(R.string.compose_home_workouts),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = Padding.x_large, start = Padding.large)
                )
                LazyRow(
                    contentPadding = PaddingValues(
                        vertical = Padding.small,
                        horizontal = Padding.large
                    ),
                    state = scrollState,
                ) {
                    items(workouts) { workout ->
                        WorkoutCard(workout) {
                            onWorkoutClick(workout)
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = Padding.large, top = Padding.gigantic),
            ) {
                Text(
                    text = stringResource(R.string.compose_home_not_right_workout),
                    style = TextStyles.body1(),
                    modifier = Modifier.padding(horizontal = Padding.tiny),
                )
                Text(
                    text = AnnotatedString(
                        text = stringResource(R.string.compose_home_create_workout_2),
                        spanStyle = SpanStyles.body1SemiboldPrimaryVariant()
                    ),
                    modifier = Modifier
                        .clickable { onCreateWorkoutClick() }
                        .padding(horizontal = Padding.tiny),
                )
            }
        }
    }
}
