package com.antsfamily.biketrainer.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antsfamily.biketrainer.ui.util.textColor

interface HistoryScreen {
    companion object {
        @Composable
        fun Content() {
            HistoryScreen()
        }
    }
}

@Composable
private fun HistoryScreen() {
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
                "Workouts history",
                fontSize = 32.sp,
                style = TextStyle(color = textColor),
                modifier = Modifier.padding(top = 64.dp)
            )
        }
    }
}
