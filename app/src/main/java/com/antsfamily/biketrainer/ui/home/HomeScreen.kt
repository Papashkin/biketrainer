package com.antsfamily.biketrainer.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.presentation.home.HomeViewModel2
import com.antsfamily.biketrainer.ui.util.textColor

interface HomeScreen {
    companion object {
        @Composable
        fun Content() {
            HomeScreen()
        }
    }
}

@Composable
private fun HomeScreen(viewModel: HomeViewModel2 = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    when (val state = uiState.value) {
        is HomeState.Content -> HomeScreenContent(state.profileName)
        HomeState.Loading -> Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun HomeScreenContent(profileName: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                "Hello, $profileName",
                fontSize = 32.sp,
                style = TextStyle(color = textColor),
                modifier = Modifier.padding(top = 64.dp)
            )
        }
    }
}
