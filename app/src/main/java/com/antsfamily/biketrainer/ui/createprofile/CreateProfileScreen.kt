package com.antsfamily.biketrainer.ui.createprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.presentation.createprofile.CreateProfileViewModel2
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen.Companion.STRING_EMPTY
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen.Companion.ZERO
import com.antsfamily.biketrainer.ui.util.textColor
import com.antsfamily.domain.antservice.orZero

interface CreateProfileScreen {
    companion object {
        @Composable
        fun Content(navigateToMain: () -> Unit) {
            CreateProfileScreen(navigateToMain)
        }

        const val STRING_EMPTY = ""
        const val ZERO = 0
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CreateProfileScreen(
    navigateToMain: () -> Unit,
    viewModel: CreateProfileViewModel2 = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val isUsernameError = (uiState.value as? CreateProfileState.TextFieldsState)?.nameError != null
    val isHeightError = (uiState.value as? CreateProfileState.TextFieldsState)?.heightError != null
    val isWeightError = (uiState.value as? CreateProfileState.TextFieldsState)?.weightError != null
    val isAgeError = (uiState.value as? CreateProfileState.TextFieldsState)?.ageError != null
    val isLoading = uiState.value is CreateProfileState.Loading

    var keyboardController = LocalSoftwareKeyboardController.current
    var username by rememberSaveable { mutableStateOf(STRING_EMPTY) }
    var height by rememberSaveable { mutableStateOf(ZERO) }
    var weight by rememberSaveable { mutableStateOf(ZERO) }
    var age by rememberSaveable { mutableStateOf(ZERO) }

    if (uiState.value is CreateProfileState.NavigateToMain) {
        keyboardController = null
        navigateToMain()
        return
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                "Create profile",
                fontSize = 32.sp,
                style = TextStyle(color = textColor),
                modifier = Modifier.padding(top = 64.dp)
            )
            OutlinedTextField(
                label = { Text("Enter your username") },
                value = username,
                onValueChange = {
                    username = it
                    viewModel.onNameChanged()
                },
                isError = isUsernameError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxSize()
            )
            if (isUsernameError) {
                Text(
                    text = (uiState.value as? CreateProfileState.TextFieldsState)?.nameError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            OutlinedTextField(
                label = { Text("Enter your height, cm") },
                value = if (height > ZERO) height.toString() else STRING_EMPTY,
                onValueChange = {
                    height = it.toIntOrNull().orZero()
                    viewModel.onHeightChanged()
                },
                singleLine = true,
                isError = isHeightError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize()
            )
            if (isHeightError) {
                Text(
                    text = (uiState.value as? CreateProfileState.TextFieldsState)?.heightError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            OutlinedTextField(
                label = { Text("Enter your weight, kg") },
                value = if (weight > ZERO) weight.toString() else STRING_EMPTY,
                onValueChange = {
                    weight = it.toIntOrNull().orZero()
                    viewModel.onWeightChanged()
                },
                singleLine = true,
                isError = isWeightError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize()
            )
            if (isWeightError) {
                Text(
                    text = (uiState.value as? CreateProfileState.TextFieldsState)?.weightError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            OutlinedTextField(
                label = { Text("Enter your age") },
                value = if (age > ZERO) age.toString() else STRING_EMPTY,
                onValueChange = {
                    age = it.toIntOrNull().orZero()
                    viewModel.onAgeChanged()
                },
                singleLine = true,
                isError = isAgeError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        viewModel.onProfileCreate(username, height, weight, age)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxSize()
            )
            if (isAgeError) {
                Text(
                    text = (uiState.value as? CreateProfileState.TextFieldsState)?.ageError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            LoadingButton(
                onClick = {
                    keyboardController?.hide()
                    viewModel.onProfileCreate(username, height, weight, age)
                },
                loading = isLoading,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Confirm")
            }
        }
    }
}

//private const val STRING_EMPTY = ""
//private const val ZERO = 0
