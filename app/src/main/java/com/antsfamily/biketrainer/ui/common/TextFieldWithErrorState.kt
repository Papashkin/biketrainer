package com.antsfamily.biketrainer.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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


@Composable
fun OutlinedTextFieldWithErrorState(
    modifier: Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = MaterialTheme.colors.surface,
        unfocusedBorderColor = MaterialTheme.colors.surface
    ),
    errorMessage: String? = null,
    onDoneClickListener: (() -> Unit)? = null
) {
    val isError = errorMessage.isNullOrBlank().not()
    Column(modifier = modifier) {
        OutlinedTextField(
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
                .fillMaxSize()
        )
        if (isError) {
            TextFieldErrorView(errorMessage)
        }
    }
}

@Composable
fun DurationOutlinedTextField(
    modifier: Modifier,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = MaterialTheme.colors.surface,
        unfocusedBorderColor = MaterialTheme.colors.surface,
        cursorColor = Color.Transparent,
    ),
    errorMessage: String? = null,
    onDoneClickListener: (() -> Unit)? = null
) {
    var textFieldValueState by remember { mutableStateOf(durationStartValue) }

    val isError = errorMessage.isNullOrBlank().not()
    Column(modifier = modifier) {
        OutlinedTextField(
            label = { Text(label) },
            value = if (textFieldValueState.isNulls()) durationStartValue else textFieldValueState,
            placeholder = { Text(DURATION_PLACEHOLDER) },
            onValueChange = {
                if (!it.isValueFull()) {
                    val formattedText = getFormattedText(it)
                    onValueChange.invoke(formattedText.text)
                    textFieldValueState = formattedText
                }
            },
            isError = isError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onDone = { onDoneClickListener?.invoke() }
            ),
            colors = colors,
            modifier = Modifier
                .fillMaxSize()
        )
        if (isError) {
            TextFieldErrorView(errorMessage)
        }
    }
}

private val durationStartValue = TextFieldValue(text = "")
private const val DURATION_PLACEHOLDER = "00:00:00"
private const val ZEROS = "000000"

fun TextFieldValue.isValueFull(): Boolean {
    val validText = "0".plus(text.filter { it.isDigit() }).take(7)
    return validText.filter { it != '0' }.length == 6
}

fun TextFieldValue.isNulls(): Boolean = text == DURATION_PLACEHOLDER

fun getFormattedText(value: TextFieldValue): TextFieldValue {
    val out = ZEROS.plus(value.text.filter { it.isDigit() }).takeLast(6)
    val hours = out.take(2)
    val minutes = out.take(4).takeLast(2)
    val seconds = out.takeLast(2)
    val duration = "$hours:$minutes:$seconds"
    return TextFieldValue(text = duration, selection = TextRange(duration.length))
}
