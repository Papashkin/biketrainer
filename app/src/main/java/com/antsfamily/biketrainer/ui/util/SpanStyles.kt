package com.antsfamily.biketrainer.ui.util

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight

object SpanStyles {

    /** Body 1 **/
    @Composable
    fun body1() = SpanStyle(fontSize = FontSize.Body1)

    @Composable
    fun body1Semibold() = body1().copy(fontWeight = FontWeight.SemiBold)

    @Composable
    fun body1Bold() = body1().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun body1Primary() = body1().copy(color = MaterialTheme.colors.primary)

    @Composable
    fun body1SemiboldPrimary() = body1Semibold().copy(
        color = MaterialTheme.colors.primary
    )

    @Composable
    fun body1SemiboldPrimaryVariant() = body1Semibold().copy(
        color = MaterialTheme.colors.primaryVariant
    )
}
