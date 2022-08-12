package com.antsfamily.biketrainer.ui.util

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable

object Shapes {

    @Composable
    fun smallRoundedShape() = RoundedCornerShape(Padding.small)

    @Composable
    fun regularRoundedShape() = RoundedCornerShape(Padding.regular)

    @Composable
    fun mediumRoundedShape() = RoundedCornerShape(Padding.medium)

    @Composable
    fun largeRoundedShape() = RoundedCornerShape(Padding.large)

}
