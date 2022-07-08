package com.antsfamily.biketrainer.ui.createprofile

import androidx.compose.foundation.layout.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.navigation.popUpToTop
import com.antsfamily.biketrainer.presentation.createprofile.CreateProfileViewModel2
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen.Companion.ZERO
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.textColor
import com.antsfamily.biketrainer.util.STRING_EMPTY
import com.antsfamily.biketrainer.util.orEmpty
import com.antsfamily.domain.antservice.orZero

interface CreateProfileScreen {
    companion object {
        @Composable
        fun Content(navController: NavController) {
            CreateProfileScreen(navController)
        }

        const val ZERO = 0
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CreateProfileScreen(
    navController: NavController,
    viewModel: CreateProfileViewModel2 = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    var keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.navigationFlow.collect {
            keyboardController?.hide()
            keyboardController = null
            navController.navigate(it) { popUpToTop(navController) }
        }
    }

    ScreenContent(uiState.value, viewModel)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenContent(
    uiState: CreateProfileState,
    viewModel: CreateProfileViewModel2,
) {
    val isUsernameError = (uiState as? CreateProfileState.TextFieldsState)?.nameError != null
    val isHeightError = (uiState as? CreateProfileState.TextFieldsState)?.heightError != null
    val isWeightError = (uiState as? CreateProfileState.TextFieldsState)?.weightError != null
    val isAgeError = (uiState as? CreateProfileState.TextFieldsState)?.ageError != null
    val isLoading = uiState is CreateProfileState.Loading

    var username by rememberSaveable { mutableStateOf(STRING_EMPTY) }
    var height by rememberSaveable { mutableStateOf(ZERO) }
    var weight by rememberSaveable { mutableStateOf(ZERO) }
    var age by rememberSaveable { mutableStateOf(ZERO) }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Padding.large)
        ) {
            Text(
                "Create profile",
                fontSize = FontSize.H4,
                style = TextStyle(color = textColor),
                modifier = Modifier.padding(top = Padding.huge)
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
                    .padding(top = Padding.x_large)
                    .fillMaxSize()
            )
            if (isUsernameError) {
                Text(
                    text = (uiState as? CreateProfileState.TextFieldsState)?.nameError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = Padding.medium)
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
                    .padding(top = Padding.large)
                    .fillMaxSize()
            )
            if (isHeightError) {
                Text(
                    text = (uiState as? CreateProfileState.TextFieldsState)?.heightError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = Padding.medium)
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
                    .padding(top = Padding.large)
                    .fillMaxSize()
            )
            if (isWeightError) {
                Text(
                    text = (uiState as? CreateProfileState.TextFieldsState)?.weightError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = Padding.medium)
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
                        viewModel.onProfileCreate(username, height, weight, age)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                modifier = Modifier
                    .padding(top = Padding.large)
                    .fillMaxSize()
            )
            if (isAgeError) {
                Text(
                    text = (uiState as? CreateProfileState.TextFieldsState)?.ageError.orEmpty(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = Padding.medium)
                )
            }
            LoadingButton(
                onClick = {
                    viewModel.onProfileCreate(username, height, weight, age)
                },
                loading = isLoading,
                modifier = Modifier.padding(top = Padding.medium)
            ) {
                Text(text = "Confirm")
            }
        }
    }
}
