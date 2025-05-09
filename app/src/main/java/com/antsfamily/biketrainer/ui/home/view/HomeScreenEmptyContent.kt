package com.antsfamily.biketrainer.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
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

@Composable
fun HomeScreenEmptyContent(
    profileName: String,
    onCreateWorkoutClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Padding.medium)
        ) {
            HomeGreetingsView(profileName)
            Box(modifier = Modifier.padding(top = Padding.gigantic)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Padding.large)
                    ) {
                        Text(
                            text = stringResource(R.string.compose_home_no_workouts),
                            style = TextStyles.body1()
                        )
                        ClickableText(
                            text = AnnotatedString(
                                text = stringResource(R.string.compose_home_create_workout_1),
                                spanStyle = SpanStyles.body1SemiboldPrimary()
                            ),
                            onClick = { onCreateWorkoutClick() },
                            modifier = Modifier.padding(top = Padding.x_large),
                        )
                    }
                }
            }
        }
    }
}
