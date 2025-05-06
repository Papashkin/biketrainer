package com.antsfamily.biketrainer.ui.util

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.util.FontSize.Body1
import com.antsfamily.biketrainer.ui.util.FontSize.Body2
import com.antsfamily.biketrainer.ui.util.FontSize.Caption
import com.antsfamily.biketrainer.ui.util.FontSize.H1
import com.antsfamily.biketrainer.ui.util.FontSize.H2
import com.antsfamily.biketrainer.ui.util.FontSize.H3
import com.antsfamily.biketrainer.ui.util.FontSize.H4
import com.antsfamily.biketrainer.ui.util.FontSize.H5
import com.antsfamily.biketrainer.ui.util.FontSize.H6

object FontSize {
    val Caption = 12.sp
    val Body2 = 14.sp
    val Body1 = 16.sp
    val H6 = 20.sp
    val H5 = 24.sp
    val H4 = 32.sp
    val H3 = 48.sp
    val H2 = 60.sp
    val H1 = 96.sp
}

val lato = FontFamily(
    Font(R.font.lato_regular, weight = FontWeight.Medium),
    Font(R.font.lato_regular, weight = FontWeight.Normal),
    Font(R.font.lato_thin, weight = FontWeight.Thin),
    Font(R.font.lato_light, weight = FontWeight.Light),
    Font(R.font.lato_bold, weight = FontWeight.Bold),
    Font(R.font.lato_black, weight = FontWeight.Black),
)

val montserrat = FontFamily(
    Font(R.font.montserrat_regular, weight = FontWeight.Normal),
    Font(R.font.montserrat_regular, weight = FontWeight.Medium),
    Font(R.font.montserrat_thin, weight = FontWeight.Thin),
    Font(R.font.montserrat_light, weight = FontWeight.Light),
    Font(R.font.montserrat_bold, weight = FontWeight.Bold),
    Font(R.font.montserrat_black, weight = FontWeight.Black),
)

val appTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = lato,
        fontWeight = FontWeight.Medium,
        fontSize = H1
    ),
    headlineMedium = TextStyle(
        fontFamily = lato,
        fontWeight = FontWeight.Medium,
        fontSize = H2
    ),
    headlineSmall = TextStyle(
        fontFamily = lato,
        fontWeight = FontWeight.Medium,
        fontSize = H3
    ),
    titleLarge = TextStyle(
        fontFamily = lato,
        fontWeight = FontWeight.Normal,
        fontSize = H4
    ),
    titleMedium = TextStyle(
        fontFamily = lato,
        fontWeight = FontWeight.Medium,
        fontSize = H5
    ),
    titleSmall = TextStyle(
        fontFamily = lato,
        fontWeight = FontWeight.Medium,
        fontSize = H6
    ),
    bodyLarge = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = H6
    ),
    bodyMedium = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = Body1
    ),
    bodySmall = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = Body2
    ),
    labelLarge = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = Body1
    ),
    labelMedium = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = Body2
    ),
    labelSmall = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = Caption
    ),
)
