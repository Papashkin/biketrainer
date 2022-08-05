package com.antsfamily.biketrainer.ui.createprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.navigation.popUpToTop
import com.antsfamily.biketrainer.presentation.createprofile.CreateProfileViewModel2
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.common.TextFieldWithErrorState
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen.Companion.ZERO
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.textColor
import com.antsfamily.biketrainer.util.STRING_EMPTY
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

    LaunchedEffect(Unit) {
        viewModel.navigationFlow.collect {
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
    val keyboardController = LocalSoftwareKeyboardController.current

    val isLoading = uiState is CreateProfileState.Loading

    var username by rememberSaveable { mutableStateOf(STRING_EMPTY) }
    var height by rememberSaveable { mutableStateOf(ZERO) }
    var weight by rememberSaveable { mutableStateOf(ZERO) }
    var age by rememberSaveable { mutableStateOf(ZERO) }

    Box {
        Image(
            painterResource(id = R.drawable.img_bg_home_cycling),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = Padding.large)
            ) {
                Text(
                    stringResource(id = R.string.compose_create_profile_title),
                    fontSize = FontSize.H4,
                    style = TextStyle(color = textColor),
                    modifier = Modifier.padding(top = Padding.huge)
                )
                TextFieldWithErrorState(
                    modifier = Modifier
                        .padding(top = Padding.large)
                        .fillMaxSize(),
                    label = stringResource(id = R.string.compose_create_profile_username),
                    value = username,
                    onValueChange = {
                        username = it
                        viewModel.onNameChanged()
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    errorMessage = (uiState as? CreateProfileState.TextFieldsState)?.nameError
                )

                TextFieldWithErrorState(
                    modifier = Modifier
                        .padding(top = Padding.regular)
                        .fillMaxSize(),
                    label = stringResource(id = R.string.compose_create_profile_height),
                    value = if (height > ZERO) height.toString() else STRING_EMPTY,
                    onValueChange = {
                        height = it.toIntOrNull().orZero()
                        viewModel.onHeightChanged()
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    keyboardType = KeyboardType.Number,
                    errorMessage = (uiState as? CreateProfileState.TextFieldsState)?.heightError
                )

                TextFieldWithErrorState(
                    modifier = Modifier
                        .padding(top = Padding.regular)
                        .fillMaxSize(),
                    label = stringResource(id = R.string.compose_create_profile_weight),
                    value = if (weight > ZERO) weight.toString() else STRING_EMPTY,
                    onValueChange = {
                        weight = it.toIntOrNull().orZero()
                        viewModel.onWeightChanged()
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    keyboardType = KeyboardType.Number,
                    errorMessage = (uiState as? CreateProfileState.TextFieldsState)?.weightError
                )

                TextFieldWithErrorState(
                    modifier = Modifier
                        .padding(top = Padding.regular)
                        .fillMaxSize(),
                    label = stringResource(id = R.string.compose_create_profile_age),
                    value = if (age > ZERO) age.toString() else STRING_EMPTY,
                    onValueChange = {
                        age = it.toIntOrNull().orZero()
                        viewModel.onAgeChanged()
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    errorMessage = (uiState as? CreateProfileState.TextFieldsState)?.ageError,
                    onDoneClickListener = {
                        keyboardController?.hide()
                        viewModel.onProfileCreateClick(username, height, weight, age)
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Padding.large,
                        end = Padding.large,
                        top = Padding.regular,
                        bottom = Padding.large
                    )
            ) {
                LoadingButton(
                    onClick = { viewModel.onProfileCreateClick(username, height, weight, age) },
                    loading = isLoading,
                    enabled = username.isNotBlank() && height > 0 && weight > 0 && age > 0,
                ) {
                    Text(
                        text = stringResource(id = R.string.compose_create_profile_create),
                        fontSize = FontSize.H6
                    )
                }
            }
        }
    }
}
