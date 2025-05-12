package com.antsfamily.biketrainer.ui.createprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.createprofile.CreateProfileViewModel
import com.antsfamily.biketrainer.ui.common.LoadingButton
import com.antsfamily.biketrainer.ui.common.TextFieldWithErrorState
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.util.orEmpty

interface CreateProfileScreen {
    companion object {
        @Composable
        fun Content(onNavigateToHome: () -> Unit) {
            CreateProfileScreen {
                onNavigateToHome()
            }
        }
    }
}

@Composable
private fun CreateProfileScreen(
    viewModel: CreateProfileViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
) {
    val uiState = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateToHomeEvent.collect {
            onNavigateToHome()
        }
    }

    ScreenContent(
        state = uiState.value,
        onNameChange = { viewModel.onNameChanged(it) },
        onAgeChange = { viewModel.onAgeChanged(it) },
        onHeightChange = { viewModel.onHeightChanged(it) },
        onWeightChange = { viewModel.onWeightChanged(it) },
        onCreateProfileButtonClick = { viewModel.onProfileCreateClick() },
    )
}

@Composable
fun ScreenContent(
    state: CreateProfileState,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onCreateProfileButtonClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
            .navigationBarsPadding()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Padding.medium)
                .verticalScroll(scrollState)
        ) {
            Text(
                stringResource(id = R.string.compose_create_profile_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = Padding.large)
            )
            Text(
                stringResource(id = R.string.compose_create_profile_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = Padding.regular)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()
            ) {
                TextFieldWithErrorState(
                    modifier = Modifier.padding(top = Padding.huge),
                    value = state.username.orEmpty(),
                    label = stringResource(id = R.string.compose_create_profile_username),
                    onValueChange = { onNameChange(it) },
                    errorMessage = if (state.isUsernameErrorVisible) {
                        stringResource(R.string.compose_create_profile_username_error)
                    } else null,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                )

                TextFieldWithErrorState(
                    modifier = Modifier.padding(top = Padding.tiny),
                    value = state.height?.toString().orEmpty(),
                    label = stringResource(id = R.string.compose_create_profile_height),
                    onValueChange = { onHeightChange(it) },
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number,
                    errorMessage = if (state.isHeightErrorVisible) {
                        stringResource(R.string.compose_create_profile_height_error)
                    } else null
                )

                TextFieldWithErrorState(
                    modifier = Modifier.padding(top = Padding.tiny),
                    value = state.weight?.toString().orEmpty(),
                    label = stringResource(id = R.string.compose_create_profile_weight),
                    onValueChange = { onWeightChange(it) },
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number,
                    errorMessage = if (state.isWeightErrorVisible) {
                        stringResource(R.string.compose_create_profile_weight_error)
                    } else null
                )

                TextFieldWithErrorState(
                    modifier = Modifier.padding(top = Padding.tiny),
                    label = stringResource(id = R.string.compose_create_profile_age),
                    value = state.age?.toString().orEmpty(),
                    onValueChange = { onAgeChange(it) },
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    errorMessage = if (state.isAgeErrorVisible) {
                        stringResource(R.string.compose_create_profile_age_error)
                    } else null,
                    onDoneClickListener = {
                        keyboardController?.hide()
                    }
                )
                HorizontalDivider(
                    thickness = Padding.huge,
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .align(Alignment.BottomCenter)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            LoadingButton(
                modifier = Modifier.padding(Padding.medium),
                onClick = { onCreateProfileButtonClick() },
                loading = state.isLoading,
                enabled = state.isCreateProfileButtonEnable,
            ) {
                Text(text = stringResource(id = R.string.compose_create_profile_create))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateProfileScreenPreview() {
    ScreenContent(CreateProfileState(age = 2), {}, {}, {}, {}) {}

}
