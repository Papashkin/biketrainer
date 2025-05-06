package com.antsfamily.biketrainer.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.util.orEmpty

@Composable
fun TextFieldWithErrorState(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    errorMessage: String? = null,
    onDoneClickListener: (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val isError = errorMessage.isNullOrBlank().not()
    TextField(
        leadingIcon = leadingIcon,
        value = value,
        onValueChange = onValueChange::invoke,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(errorMessage.orEmpty(), style = MaterialTheme.typography.labelSmall)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = { onDoneClickListener?.invoke() }
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(Padding.regular),
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TextFieldWithErrorStatePreview() {
    Column {
        TextFieldWithErrorState(
            value = "John Doe",
            onValueChange = {}
        )
        TextFieldWithErrorState(
            value = "John Doe",
            errorMessage = "Error occurred here",
            onValueChange = {}
        )

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
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    ),
    errorMessage: String? = null,
    enabled: Boolean = true,
    onDoneClickListener: (() -> Unit)? = null
) {
    val isError = errorMessage.isNullOrBlank().not()
    Column(modifier = modifier) {
        OutlinedTextField(
            label = { Text(label) },
            value = value,
            onValueChange = onValueChange::invoke,
            enabled = enabled,
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
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
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
