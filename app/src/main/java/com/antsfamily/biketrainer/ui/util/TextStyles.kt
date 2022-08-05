package com.antsfamily.biketrainer.ui.util

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

object TextStyles {

    /** Body 1 **/
    @Composable
    fun body1() = TextStyle(fontSize = FontSize.Body1)

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

    /** Header 6 **/
    @Composable
    fun header6() = TextStyle(fontSize = FontSize.H6)

    @Composable
    fun header6Semibold() = header6().copy(fontWeight = FontWeight.SemiBold)

    @Composable
    fun header6Bold() = header6().copy(fontWeight = FontWeight.Bold)

    @Composable
    fun header6Primary() = header6().copy(color = MaterialTheme.colors.primary)

    @Composable
    fun header6SemiboldPrimary() = header6Semibold().copy(
        color = MaterialTheme.colors.primary
    )
    @Composable
    fun header6SemiboldPrimaryVariant() = header6Semibold().copy(
        color = MaterialTheme.colors.primaryVariant
    )

}
