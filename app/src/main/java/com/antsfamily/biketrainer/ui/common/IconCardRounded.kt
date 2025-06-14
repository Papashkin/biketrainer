package com.antsfamily.biketrainer.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.util.Padding

@Composable
fun IconCardRounded(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceDim,
    imageSize: Dp = 48.dp,
    cardRadius: Dp = 6.dp
) {

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(cardRadius)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .padding(Padding.x_small)
        )
    }

}

@Preview
@Composable
private fun IconCardRoundedPreview() {
    IconCardRounded(imageRes = R.drawable.ic_workout)
}