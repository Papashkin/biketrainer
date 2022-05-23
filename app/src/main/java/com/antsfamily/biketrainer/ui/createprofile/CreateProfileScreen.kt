package com.antsfamily.biketrainer.ui.createprofile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.presentation.createprofile.CreateProfileViewModel2
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.util.textColor
import com.antsfamily.domain.antservice.orZero

interface CreateProfileScreen {
    companion object {
        @Composable
        fun Content(navigateToMain: (profile: String) -> Unit) {
            CreateProfileScreen(navigateToMain)
        }
    }
}

@Composable
fun CreateProfileScreen(
    navigateToMain: (profile: String) -> Unit,
    viewModel: CreateProfileViewModel2 = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState(CreateProfileState.Initial)

    if (uiState.value is CreateProfileState.NavigateToMain) {
        navigateToMain((uiState.value as CreateProfileState.NavigateToMain).profileName)
    }

    var username by rememberSaveable { mutableStateOf("") }
    var isUsernameError by rememberSaveable { mutableStateOf(false) }
    var height by rememberSaveable { mutableStateOf(0L) }
    var isHeightError by rememberSaveable { mutableStateOf(false) }
    var weight by rememberSaveable { mutableStateOf(0L) }
    var isWeightError by rememberSaveable { mutableStateOf(false) }
    var age by rememberSaveable { mutableStateOf(0) }
    var isAgeError by rememberSaveable { mutableStateOf(false) }

    isUsernameError = (uiState.value as? CreateProfileState.TextFieldsState)?.nameError != null
    isHeightError = (uiState.value as? CreateProfileState.TextFieldsState)?.heightError != null
    isWeightError = (uiState.value as? CreateProfileState.TextFieldsState)?.weightError != null
    isAgeError = (uiState.value as? CreateProfileState.TextFieldsState)?.ageError != null

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
                value = if (height > 0L) height.toString() else "",
                onValueChange = {
                    height = it.toLongOrNull().orZero()
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
                value = if (weight > 0L) weight.toString() else "",
                onValueChange = {
                    weight = it.toLongOrNull().orZero()
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
                value = if (age > 0L) age.toString() else "",
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
                onClick = { viewModel.createProfile(username, height, weight, age) },
                loading = uiState.value == CreateProfileState.Loading,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Confirm")
            }
        }
    }
}
