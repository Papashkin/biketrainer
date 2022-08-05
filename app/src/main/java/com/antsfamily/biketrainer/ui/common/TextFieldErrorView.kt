package com.antsfamily.biketrainer.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.util.orEmpty

@Composable
fun TextFieldErrorView(error: String?) {
    Text(
        text = error.orEmpty(),
        color = MaterialTheme.colors.error,
        style = TextStyle(fontSize = FontSize.Caption, fontWeight = FontWeight.Medium),
        modifier = Modifier.padding(start = Padding.medium)
    )
}
