package com.antsfamily.biketrainer.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlusOne
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.presentation.home.HomeViewModel2
import com.antsfamily.biketrainer.ui.common.WorkoutChart
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.primaryColor
import com.antsfamily.biketrainer.ui.util.textColor
import com.antsfamily.data.model.program.Program

interface HomeScreen {
    companion object {
        @Composable
        fun Content(navController: NavController) {
            HomeScreen(navController)
        }
    }
}

@Composable
private fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel2 = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    when (val state = uiState.value) {
        HomeState.Loading -> FullScreenLoading()
        is HomeState.EmptyContent -> HomeScreenEmptyContent(state.profileName, viewModel)
        is HomeState.ContentWithData -> HomeScreenContentWithData(
            state.profileName,
            state.workouts,
            viewModel
        )
    }

    LaunchedEffect(Unit) {
        viewModel.navigationFlow.collect {
            navController.navigate(it)
        }
    }
}

@Composable
fun HomeScreenEmptyContent(profileName: String, viewModel: HomeViewModel2) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Padding.large)
        ) {
            Text(
                "Hello, $profileName!",
                fontSize = FontSize.H4,
                style = TextStyle(color = textColor),
                modifier = Modifier.padding(top = Padding.huge)
            )

            Box(modifier = Modifier.padding(top = Padding.gigantic)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Padding.large)
                    ) {
                        Text(text = "There is no workouts available", fontSize = FontSize.Body1)
                        ClickableText(
                            text = AnnotatedString(
                                "Create a workout",
                                spanStyle = SpanStyle(
                                    fontSize = FontSize.Body1,
                                    color = primaryColor,
                                    fontWeight = FontWeight.SemiBold
                                )
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenContentWithData(
    profileName: String,
    workouts: List<Program>,
    viewModel: HomeViewModel2
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateWorkoutClick() },
                modifier = Modifier.padding(bottom = Padding.huge)
            ) {
                Icon(imageVector = Icons.Rounded.PlusOne, contentDescription = null)
            }
        },
        content = {
            Column {
                Text(
                    "Hello, $profileName!",
                    fontSize = FontSize.H4,
                    style = TextStyle(color = textColor),
                    modifier = Modifier.padding(top = Padding.huge, start = Padding.large)
                )
                Text(
                    "Here are your workouts:",
                    fontSize = FontSize.H6,
                    style = TextStyle(color = textColor),
                    modifier = Modifier.padding(top = Padding.large, start = Padding.large)
                )
                LazyColumn(
                    contentPadding = PaddingValues(Padding.medium),
                    verticalArrangement = Arrangement.spacedBy(Padding.regular),
                    state = scrollState
                ) {
                    items(workouts) { workout ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.onWorkoutClick(workout) }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                WorkoutChart(
                                    width = 120f,
                                    height = 120f,
                                    modifier = Modifier.padding(Padding.medium),
                                    workoutSteps = workout.data
                                )
                                Column(modifier = Modifier.padding(Padding.regular)) {
                                    Text(
                                        text = workout.title,
                                        fontSize = FontSize.H6,
                                        modifier = Modifier.padding(Padding.tiny)
                                    )
                                    Text(
                                        text = workout.title,
                                        fontSize = FontSize.Body1,
                                        modifier = Modifier.padding(Padding.tiny)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
//    Column {
//        Text(
//            "Hello, $profileName!",
//            fontSize = FontSize.H4,
//            style = TextStyle(color = textColor),
//            modifier = Modifier.padding(top = Padding.huge, start = Padding.large)
//        )
//        LazyColumn(
//            contentPadding = PaddingValues(Padding.medium),
//            verticalArrangement = Arrangement.spacedBy(Padding.regular),
//            state = scrollState
//        ) {
//            items(workouts) { workout ->
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    onClick = { viewModel.onWorkoutClick(workout) }
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Start
//                    ) {
//                        WorkoutChart(
//                            width = 120f,
//                            height = 120f,
//                            modifier = Modifier.padding(Padding.medium),
//                            workoutSteps = workout.data
//                        )
//                        Column(modifier = Modifier.padding(Padding.regular)) {
//                            Text(
//                                text = workout.title,
//                                fontSize = FontSize.H6,
//                                modifier = Modifier.padding(Padding.tiny)
//                            )
//                            Text(
//                                text = workout.title,
//                                fontSize = FontSize.Body1,
//                                modifier = Modifier.padding(Padding.tiny)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//    FloatingActionButton(
//        onClick = { viewModel.onCreateWorkoutClick() },
//        modifier = Modifier.padding(top = Padding.medium)
//    ) {
//        Icon(imageVector = Icons.Rounded.PlusOne, contentDescription = null)
//    }
}

@Composable
fun FullScreenLoading() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
        }
    }
}
