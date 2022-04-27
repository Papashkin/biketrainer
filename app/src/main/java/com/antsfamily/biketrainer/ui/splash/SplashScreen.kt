package com.antsfamily.biketrainer.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import com.antsfamily.biketrainer.R

interface SplashScreen {
    companion object {
        @Composable
        fun Content() {
            SplashScreen()
        }
    }
}

@Composable
fun SplashScreen() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(model = R.drawable.ic_cycling_road, contentDescription = null)
            CircularProgressIndicator()
        }
    }
}
