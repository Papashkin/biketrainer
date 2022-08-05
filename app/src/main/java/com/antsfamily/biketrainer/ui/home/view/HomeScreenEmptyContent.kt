package com.antsfamily.biketrainer.ui.home.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.home.HomeViewModel2
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.SpanStyles
import com.antsfamily.biketrainer.ui.util.TextStyles

@Composable
fun HomeScreenEmptyContent(profileName: String, viewModel: HomeViewModel2) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Padding.large)
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
                            onClick = { viewModel.onCreateWorkoutClick() },
                            modifier = Modifier.padding(top = Padding.xx_large),
                        )
                    }
                }
            }
        }
    }
}
