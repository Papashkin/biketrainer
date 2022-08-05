package com.antsfamily.biketrainer.ui.home.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding

@Composable
fun HomeGreetingsView(username: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.compose_home_title, username),
        fontSize = FontSize.H4,
        modifier = modifier
            .padding(vertical = Padding.huge, horizontal = Padding.large)
            .fillMaxWidth()
    )
}
