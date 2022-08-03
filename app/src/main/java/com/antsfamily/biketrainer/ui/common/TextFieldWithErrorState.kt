package com.antsfamily.biketrainer.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.util.orEmpty

@Composable
fun TextFieldWithErrorState(
    modifier: Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.surface,
    ),
    errorMessage: String? = null,
    onDoneClickListener: (() -> Unit)? = null
) {
    val isError = errorMessage.isNullOrBlank().not()
    Column(modifier = modifier) {
        TextField(
            label = { Text(label) },
            value = value,
            onValueChange = onValueChange::invoke,
            isError = isError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onDone = { onDoneClickListener?.invoke() }
            ),
            colors = colors,
            modifier = Modifier
                .padding(top = Padding.large)
                .fillMaxSize()
        )
        if (isError) {
            Text(
                text = errorMessage.orEmpty(),
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = Padding.medium)
            )
        }
    }
}
