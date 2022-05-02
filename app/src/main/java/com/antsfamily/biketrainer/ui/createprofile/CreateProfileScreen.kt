package com.antsfamily.biketrainer.ui.createprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

interface CreateProfileScreen {
    companion object {
        @Composable
        fun Content() {
            CreateProfileScreen()
        }
    }
}

@Composable
fun CreateProfileScreen() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "CreateProfileScreen")
        }
    }
}
