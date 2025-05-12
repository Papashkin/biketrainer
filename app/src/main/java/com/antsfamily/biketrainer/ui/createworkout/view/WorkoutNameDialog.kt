package com.antsfamily.biketrainer.ui.createworkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.common.TextFieldWithErrorState
import com.antsfamily.biketrainer.ui.util.Padding

private const val WORKOUT_NAME_LENGTH_LIMIT = 20

@Composable
fun WorkoutNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onNameChanged: (String) -> Unit
) {
    val (name, setName) = rememberSaveable { mutableStateOf(currentName) }

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Padding.medium),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .padding(Padding.large)
                        .background(MaterialTheme.colorScheme.surfaceDim, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_settings_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(Padding.small)
                    )
                }
                Text(
                    text = "Please insert your workout name here ($WORKOUT_NAME_LENGTH_LIMIT symbols max.)",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(Padding.small),
                )

                TextFieldWithErrorState(
                    value = name,
                    label = "workout name",
                    onValueChange = { setName(it) },
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                    errorMessage = if (name.isBlank() || name.length > WORKOUT_NAME_LENGTH_LIMIT) {
                        "${name.length}/$WORKOUT_NAME_LENGTH_LIMIT"
                    } else
                        null
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(Padding.small),
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(
                        onClick = { onNameChanged(name) },
                        enabled = name.isNotBlank() && name.length <= WORKOUT_NAME_LENGTH_LIMIT,
                        modifier = Modifier.padding(Padding.small),
                    ) {
                        Text(
                            text = "Confirm",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WorkoutNameDialogPreview() {
    WorkoutNameDialog("asNTeaslkdjasdasdasd", {}, {})
}