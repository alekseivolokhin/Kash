package com.volokhinaleksey.kash.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private fun lightSchemeFor(c: KashColors) = lightColorScheme(
    primary = c.accent,
    onPrimary = c.accentInk,
    primaryContainer = c.accentSoft,
    onPrimaryContainer = c.accentSoftInk,
    secondary = c.accent,
    onSecondary = c.accentInk,
    secondaryContainer = c.accentSoft,
    onSecondaryContainer = c.accentSoftInk,
    background = c.bg,
    onBackground = c.text,
    surface = c.card,
    onSurface = c.text,
    surfaceVariant = c.cardAlt,
    onSurfaceVariant = c.sub,
    surfaceContainerLowest = c.card,
    surfaceContainerLow = c.card,
    surfaceContainer = c.cardAlt,
    surfaceContainerHigh = c.chipBg,
    surfaceContainerHighest = c.chipBg,
    outline = c.lineStrong,
    outlineVariant = c.line,
    error = c.neg,
    onError = c.accentInk,
)

private fun darkSchemeFor(c: KashColors) = darkColorScheme(
    primary = c.accent,
    onPrimary = c.accentInk,
    primaryContainer = c.accentSoft,
    onPrimaryContainer = c.accentSoftInk,
    secondary = c.accent,
    onSecondary = c.accentInk,
    secondaryContainer = c.accentSoft,
    onSecondaryContainer = c.accentSoftInk,
    background = c.bg,
    onBackground = c.text,
    surface = c.card,
    onSurface = c.text,
    surfaceVariant = c.cardAlt,
    onSurfaceVariant = c.sub,
    surfaceContainerLowest = c.card,
    surfaceContainerLow = c.card,
    surfaceContainer = c.cardAlt,
    surfaceContainerHigh = c.chipBg,
    surfaceContainerHighest = c.chipBg,
    outline = c.lineStrong,
    outlineVariant = c.line,
    error = c.neg,
    onError = c.accentInk,
)

@Composable
fun KashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val palette = if (darkTheme) KashDarkColors else KashLightColors
    val colorScheme = if (darkTheme) darkSchemeFor(palette) else lightSchemeFor(palette)

    ApplySystemBarsStyle(darkTheme = darkTheme)

    CompositionLocalProvider(LocalKashColors provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = KashTypography(),
            content = content,
        )
    }
}
