package com.antsfamily.biketrainer.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.ReplayCircleFilled
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.presentation.settings.SettingsViewModel
import com.antsfamily.biketrainer.ui.util.FontSize
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.Shapes
import com.antsfamily.data.model.Circumference

interface SettingsScreen {
    companion object {
        @Composable
        fun Content() {
            SettingsScreen()
        }
    }
}

@Composable
private fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    var isCircumferenceExpanded by rememberSaveable { mutableStateOf(false) }

    val state = viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            stringResource(R.string.compose_settings_title),
            fontSize = FontSize.H4,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colors.surface,
                            MaterialTheme.colors.surface,
                            MaterialTheme.colors.background
                        ),
                    ),
                )
                .padding(vertical = Padding.huge, horizontal = Padding.large)
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .padding(top = Padding.x_small)
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
        ) {
            Text(
                stringResource(R.string.compose_settings_profile),
                fontSize = FontSize.H6,
                modifier = Modifier.padding(
                    top = Padding.x_large,
                    start = Padding.large,
                    end = Padding.large
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Padding.x_large,
                        start = Padding.large,
                        end = Padding.large
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = Shapes.smallRoundedShape()
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Column(modifier = Modifier.padding(horizontal = Padding.large)) {
                    Text(
                        text = state.value.username,
                        fontSize = FontSize.Body1,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = Padding.x_small)
                    )
                    Text(
                        text = "Personal info",
                        fontSize = FontSize.Caption,
                        modifier = Modifier.padding(top = Padding.tiny)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(Padding.medium)
                        .clip(Shapes.smallRoundedShape())
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = Shapes.smallRoundedShape()
                        )
                        .clickable {
                            //TODO add personal info screen
                        }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Text(
                stringResource(R.string.compose_settings),
                fontSize = FontSize.H6,
                modifier = Modifier.padding(
                    top = Padding.xx_large,
                    start = Padding.large,
                    end = Padding.large
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Padding.x_large,
                        start = Padding.large,
                        end = Padding.large
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = Shapes.smallRoundedShape()
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ReplayCircleFilled,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Column(modifier = Modifier.padding(horizontal = Padding.large)) {
                    Text(
                        text = state.value.wheelCircumference.title ?: "-",
                        fontSize = FontSize.Body1,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = Padding.x_small)
                    )
                    Text(
                        text = stringResource(R.string.compose_settings_circumference_label),
                        fontSize = FontSize.Caption,
                        modifier = Modifier.padding(top = Padding.tiny)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(Padding.medium)
                        .clip(Shapes.smallRoundedShape())
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = Shapes.smallRoundedShape()
                        )
                        .clickable {
                            isCircumferenceExpanded = !isCircumferenceExpanded
                        }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    DropdownMenu(
                        expanded = isCircumferenceExpanded,
                        onDismissRequest = { isCircumferenceExpanded = false }
                    ) {
                        Circumference.values().forEach {
                            DropdownMenuItem(
                                modifier = Modifier.fillMaxSize(),
                                onClick = {
                                    viewModel.isCircumferenceClicked(it)
                                    isCircumferenceExpanded = false
                                }) {
                                Text(text = it.toString())
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Padding.large,
                        start = Padding.large,
                        end = Padding.large
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = Shapes.smallRoundedShape()
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DarkMode,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Column(modifier = Modifier.padding(horizontal = Padding.large)) {
                    Text(
                        text = if (state.value.isDarkModeEnabled) "On" else "Off",
                        fontSize = FontSize.Body1,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = Padding.x_small)
                    )
                    Text(
                        text = stringResource(R.string.compose_settings_dark_mode),
                        fontSize = FontSize.Caption,
                        modifier = Modifier.padding(top = Padding.tiny)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.padding(end = Padding.small),
                    checked = state.value.isDarkModeEnabled,
                    onCheckedChange = { viewModel.onUiModeChanged(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primaryVariant,
                        checkedTrackColor = MaterialTheme.colors.primaryVariant
                    )
                )
            }
        }
    }
}
