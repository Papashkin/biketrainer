package com.antsfamily.biketrainer.ui.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.home.HomeViewModel2
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.SpanStyles
import com.antsfamily.biketrainer.ui.util.TextStyles
import com.antsfamily.data.model.program.Program

@Composable
fun HomeScreenContentWithData(
    profileName: String,
    workouts: List<Program>,
    viewModel: HomeViewModel2
) {
    val scrollState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxWidth()) {
        HomeGreetingsView(
            profileName,
            Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colors.surface,
                        MaterialTheme.colors.surface,
                        MaterialTheme.colors.background
                    ),
                ),
            )
        )
        Column(
            modifier = Modifier
                .padding(top = Padding.x_small)
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.compose_home_workouts),
                    style = TextStyles.header6Bold(),
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
                            viewModel.onWorkoutClick(workout)
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
                ClickableText(
                    text = AnnotatedString(
                        text = stringResource(R.string.compose_home_create_workout_2),
                        spanStyle = SpanStyles.body1SemiboldPrimaryVariant()
                    ),
                    onClick = { viewModel.onCreateWorkoutClick() },
                    modifier = Modifier.padding(horizontal = Padding.tiny),
                )
            }
        }
    }
}
