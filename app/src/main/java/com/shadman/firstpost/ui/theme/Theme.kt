package com.shadman.firstpost.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// One fixed dark palette. No light theme and no Material You dynamic colour,
// so the app looks identical on every phone, like the real VMP app.
private val VmpColorScheme = darkColorScheme(
    primary = VmpBlue,
    onPrimary = TextPrimary,
    primaryContainer = VmpBlueContainer,
    onPrimaryContainer = VmpBlue,
    secondary = VmpBlue,
    onSecondary = TextPrimary,
    background = NavyBackground,
    onBackground = TextPrimary,
    surface = NavySurface,
    onSurface = TextPrimary,
    surfaceVariant = IndigoCard,
    onSurfaceVariant = TextSecondary,
    outline = OutlineNavy,
    outlineVariant = CardBorder,
)

@Composable
fun FirstPostTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VmpColorScheme,
        typography = Typography,
        shapes = VmpShapes,
        content = content,
    )
}
