package com.antsfamily.biketrainer.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.presentation.home.HomeViewModel2
import com.antsfamily.biketrainer.ui.common.FullScreenLoading
import com.antsfamily.biketrainer.ui.common.WorkoutChart
import com.antsfamily.biketrainer.ui.util.*
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
    Column(modifier = Modifier.fillMaxWidth()) {
        HomeGreetingsView(
            profileName,
            Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(surfaceColor, surfaceColor, backgroundColor),
                ),
            )
        )
        Column(
            modifier = Modifier
                .padding(top = Padding.x_small)
                .fillMaxWidth()
                .background(backgroundColor)
        ) {
            Column {
                Text(
                    "Workouts",
                    fontSize = FontSize.H6,
                    style = TextStyle(color = textColor, fontWeight = FontWeight.Bold),
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
                    text = "Don't have right workout?",
                    modifier = Modifier.padding(horizontal = Padding.tiny),
                    fontSize = FontSize.Body1,
                    style = TextStyle(color = textColor, fontWeight = FontWeight.Normal),
                )
                ClickableText(
                    text = AnnotatedString(
                        "Create new one",
                        spanStyle = SpanStyle(
                            fontSize = FontSize.Body1,
                            color = primaryVariantColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    ),
                    onClick = { viewModel.onCreateWorkoutClick() },
                    modifier = Modifier.padding(horizontal = Padding.tiny),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutCard(
    workout: Program,
    onWorkoutClick: (Program) -> Unit
) {
    Card(
        modifier = Modifier.padding(start = Padding.tiny, end = Padding.small),
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        onClick = { onWorkoutClick(workout) }
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            WorkoutChart(
                width = 200f,
                height = 200f,
                shape = RoundedCornerShape(4.dp),
                workoutSteps = workout.data
            )
            Column {
                Text(
                    text = workout.title,
                    fontSize = FontSize.H5,
                    style = TextStyle(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(Padding.tiny)
                )
                Text(
                    text = workout.getAveragePower().toString().plus(" W avg."),
                    fontSize = FontSize.Body1,
                    modifier = Modifier.padding(start = Padding.tiny, top = Padding.small)
                )
                Text(
                    text = workout.getDuration(),
                    fontSize = FontSize.Caption,
                    modifier = Modifier.padding(Padding.tiny)
                )
            }
        }
    }
}

@Composable
fun HomeGreetingsView(username: String, modifier: Modifier = Modifier) {
    Text(
        "Hello, $username",
        fontSize = FontSize.H4,
        modifier = modifier
            .padding(vertical = Padding.huge, horizontal = Padding.large)
            .fillMaxWidth()
    )
}
