package com.antsfamily.biketrainer.ui.createprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.createprofile.CreateProfileViewModel2
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.common.TextFieldWithErrorState
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileScreen.Companion.ZERO
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.appTypography
import com.antsfamily.biketrainer.util.STRING_EMPTY
import com.antsfamily.domain.antservice.orZero

interface CreateProfileScreen {
    companion object {
        @Composable
        fun Content(onNavigate: (String) -> Unit) {
            CreateProfileScreen {
                onNavigate(it)
            }
        }

        const val ZERO = 0
    }
}

@Composable
private fun CreateProfileScreen(
    viewModel: CreateProfileViewModel2 = hiltViewModel(),
    onNavigate: (String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationFlow.collect {
            onNavigate(it)
        }
    }
    ScreenContent(uiState.value, viewModel)
}

@Composable
fun ScreenContent(
    uiState: CreateProfileState,
    viewModel: CreateProfileViewModel2,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var username by rememberSaveable { mutableStateOf(STRING_EMPTY) }
    var height by rememberSaveable { mutableStateOf(ZERO) }
    var weight by rememberSaveable { mutableStateOf(ZERO) }
    var age by rememberSaveable { mutableStateOf(ZERO) }

    Box {
        Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = Padding.medium)
            ) {
                Text(
                    stringResource(id = R.string.compose_create_profile_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = Padding.huge)
                )
                Text(
                    stringResource(id = R.string.compose_create_profile_username),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Padding.huge)
                )
                TextFieldWithErrorState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Padding.x_small),
                    value = username,
                    onValueChange = {
                        username = it
                        viewModel.onNameChanged()
                    },
                    errorMessage = uiState.nameError
                )

                Text(
                    stringResource(id = R.string.compose_create_profile_height),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Padding.small)
                )
                TextFieldWithErrorState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Padding.x_small),
                    value = if (height > ZERO) height.toString() else STRING_EMPTY,
                    onValueChange = {
                        height = it.toIntOrNull().orZero()
                        viewModel.onHeightChanged()
                    },
                    keyboardType = KeyboardType.Number,
                    errorMessage = uiState.heightError
                )

                Text(
                    stringResource(id = R.string.compose_create_profile_weight),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Padding.small)
                )
                TextFieldWithErrorState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Padding.x_small),
                    value = if (weight > ZERO) weight.toString() else STRING_EMPTY,
                    onValueChange = {
                        weight = it.toIntOrNull().orZero()
                        viewModel.onWeightChanged()
                    },
                    keyboardType = KeyboardType.Number,
                    errorMessage = uiState.weightError
                )

                Text(
                    stringResource(id = R.string.compose_create_profile_age),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Padding.small)
                )
                TextFieldWithErrorState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Padding.x_small),
                    value = if (age > ZERO) age.toString() else STRING_EMPTY,
                    onValueChange = {
                        age = it.toIntOrNull().orZero()
                        viewModel.onAgeChanged()
                    },
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    errorMessage = uiState.ageError,
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
                    loading = uiState.isLoading,
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

@Preview(showBackground = true)
@Composable
private fun CreateProfileScreenPreview() {
    CreateProfileScreen() {}

}
