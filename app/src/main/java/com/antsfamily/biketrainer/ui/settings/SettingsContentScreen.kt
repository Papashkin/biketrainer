package com.antsfamily.biketrainer.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.core.model.Circumference
import com.antsfamily.biketrainer.core.model.toStringId
import com.antsfamily.biketrainer.presentation.settings.SettingsUiState
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.biketrainer.ui.util.Shapes


@Composable
fun SettingsContentScreen(
    state: SettingsUiState.Content,
    onCircumferenceChanged: (Circumference) -> Unit,
    onThemeChanged: (Boolean) -> Unit,
) {
    var isCircumferenceExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Padding.medium)
    ) {
        Text(
            stringResource(R.string.compose_settings_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Padding.huge)
        )

        Column(
            modifier = Modifier
                .padding(top = Padding.x_small)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                stringResource(R.string.compose_settings_profile),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = Padding.x_large)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Padding.large)
            ) {
                Box(modifier = Modifier.size(60.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_settings_profile),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Column(modifier = Modifier.padding(horizontal = Padding.large)) {
                    Text(
                        text = state.username,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = Padding.x_small)
                    )
                    Text(
                        text = "Personal info",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = Padding.tiny)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(Padding.medium)
                        .clip(Shapes.smallRoundedShape())
                        .clickable {
                            //TODO add personal info screen
                        }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Text(
                stringResource(R.string.compose_settings),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = Padding.xx_large)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Padding.large)
            ) {
                Box(modifier = Modifier.size(60.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_settings_wheel),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Column(modifier = Modifier.padding(horizontal = Padding.large)) {
                    Text(
                        text = stringResource(state.wheelCircumference.toStringId()),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = Padding.x_small)
                    )
                    Text(
                        text = stringResource(R.string.compose_settings_circumference_label),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = Padding.tiny)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(Padding.medium)
                        .clip(Shapes.smallRoundedShape())
                        .clickable {
                            isCircumferenceExpanded = !isCircumferenceExpanded
                        }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    DropdownMenu(
                        expanded = isCircumferenceExpanded,
                        onDismissRequest = { isCircumferenceExpanded = false }
                    ) {
                        Circumference.entries.forEach {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(it.toStringId())) },
                                modifier = Modifier.fillMaxSize(),
                                onClick = {
                                    onCircumferenceChanged(it)
                                    isCircumferenceExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Padding.large)
            ) {
                Box(modifier = Modifier.size(60.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_settings_palette),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Column(modifier = Modifier.padding(horizontal = Padding.large)) {
                    Text(
                        text = if (state.isDarkModeEnabled) "On" else "Off",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = Padding.x_small)
                    )
                    Text(
                        text = stringResource(R.string.compose_settings_dark_mode),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = Padding.tiny)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.padding(end = Padding.small),
                    checked = state.isDarkModeEnabled,
                    onCheckedChange = { onThemeChanged(it) },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsContentScreenPreview() {
    SettingsContentScreen(
        state = SettingsUiState.Content(
            "John Doe",
            Circumference.WHEEL_700x28,
            true
        ),
        {},
        {}
    )
}