package com.unifytv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Typography
import androidx.tv.material3.darkColorScheme

private val UnifyColors = darkColorScheme(
    primary = Violet,
    onPrimary = Cloud,
    secondary = Teal,
    onSecondary = Ink,
    tertiary = Coral,
    background = Ink,
    onBackground = Cloud,
    surface = Midnight,
    onSurface = Cloud,
    surfaceVariant = Plum,
    onSurfaceVariant = Mist,
    border = GlassStrong,
)

private val UnifyType = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 48.sp, letterSpacing = (-0.5).sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 28.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 13.sp, letterSpacing = 0.5.sp),
)

@Composable
fun UnifyTvTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = UnifyColors,
        typography = UnifyType,
        content = content,
    )
}
