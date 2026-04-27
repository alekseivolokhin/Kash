package com.volokhinaleksey.kash.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class KashColors(
    val bg: Color,
    val card: Color,
    val cardAlt: Color,
    val chipBg: Color,
    val text: Color,
    val sub: Color,
    val fade: Color,
    val line: Color,
    val lineStrong: Color,
    val accent: Color,
    val accentInk: Color,
    val accentSoft: Color,
    val accentSoftInk: Color,
    val pos: Color,
    val neg: Color,
    val bankNeutralBg: Color,
    val bankNeutralFg: Color,
    val warn: Color,
    val warnSoft: Color,
    val isDark: Boolean,
)

val KashLightColors = KashColors(
    bg = Color(0xFFF2EEE5),
    card = Color(0xFFFBF8F2),
    cardAlt = Color(0xFFEBE5D8),
    chipBg = Color(0xFFE5E0D2),
    text = Color(0xFF1B1F1A),
    sub = Color(0xFF6B6F66),
    fade = Color(0xFFA8AA9E),
    line = Color(0x141B1F1A),
    lineStrong = Color(0x241B1F1A),
    accent = Color(0xFF1F3D2C),
    accentInk = Color(0xFFFBF8F2),
    accentSoft = Color(0xFFDCE2D2),
    accentSoftInk = Color(0xFF1F3D2C),
    pos = Color(0xFF3D7A4A),
    neg = Color(0xFFB0473A),
    bankNeutralBg = Color(0xFFE8E4D6),
    bankNeutralFg = Color(0xFF3A3A33),
    warn = Color(0xFFA86E1F),
    warnSoft = Color(0xFFF4E5C8),
    isDark = false,
)

val KashDarkColors = KashColors(
    bg = Color(0xFF161513),
    card = Color(0xFF1F1E1B),
    cardAlt = Color(0xFF262521),
    chipBg = Color(0xFF262521),
    text = Color(0xFFF1ECE0),
    sub = Color(0xFF9B988E),
    fade = Color(0xFF6A6760),
    line = Color(0x12F1ECE0),
    lineStrong = Color(0x1FF1ECE0),
    accent = Color(0xFF5A8C66),
    accentInk = Color(0xFFFBF8F2),
    accentSoft = Color(0x2E5A8C66),
    accentSoftInk = Color(0xFF9CC6A4),
    pos = Color(0xFF7FB089),
    neg = Color(0xFFD27F73),
    bankNeutralBg = Color(0x1AF1ECE0),
    bankNeutralFg = Color(0xFFC8C2B0),
    warn = Color(0xFFD5A865),
    warnSoft = Color(0x29D5A865),
    isDark = true,
)

val LocalKashColors = staticCompositionLocalOf { KashLightColors }

object Kash {
    val colors: KashColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKashColors.current
}

data class CategorySwatch(val bg: Color, val fg: Color)

data class CategoryPalette(
    val food: CategorySwatch,
    val transport: CategorySwatch,
    val entertainment: CategorySwatch,
    val income: CategorySwatch,
    val shopping: CategorySwatch,
    val electronics: CategorySwatch,
    val subscriptions: CategorySwatch,
)

val LightCategoryPalette = CategoryPalette(
    food = CategorySwatch(Color(0xFFF4E9DC), Color(0xFF7A4A1E)),
    transport = CategorySwatch(Color(0xFFE2E9F0), Color(0xFF2C4A66)),
    entertainment = CategorySwatch(Color(0xFFEFE4F0), Color(0xFF5C3A66)),
    income = CategorySwatch(Color(0xFFDCE5DD), Color(0xFF2E4A36)),
    shopping = CategorySwatch(Color(0xFFF4DFDC), Color(0xFF7A3A30)),
    electronics = CategorySwatch(Color(0xFFE5E7EA), Color(0xFF3A4048)),
    subscriptions = CategorySwatch(Color(0xFFEAE6DC), Color(0xFF5A4E2A)),
)

val DarkCategoryPalette = CategoryPalette(
    food = CategorySwatch(Color(0x2EC48E52), Color(0xFFD6A871)),
    transport = CategorySwatch(Color(0x2E6E96C4), Color(0xFF9CB7D8)),
    entertainment = CategorySwatch(Color(0x29AA78C8), Color(0xFFC29ED6)),
    income = CategorySwatch(Color(0x297FB089), Color(0xFF9CC6A4)),
    shopping = CategorySwatch(Color(0x29C46C60), Color(0xFFD89E96)),
    electronics = CategorySwatch(Color(0x2996A0AA), Color(0xFFB0B8C0)),
    subscriptions = CategorySwatch(Color(0x29B4A064), Color(0xFFC8B68A)),
)
