package com.volokhinaleksey.kash.designsystem.bank

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

enum class Bank(
    val id: String,
    val displayName: String,
    val initial: String,
    val country: String?,
) {
    Kaspi("kaspi", "Kaspi Bank", "k", "Kazakhstan"),
    Halyk("halyk", "Halyk Bank", "H", "Kazakhstan"),
    Forte("forte", "ForteBank", "F", "Kazakhstan"),
    Jusan("jusan", "Jusan Bank", "j", "Kazakhstan"),
    Atf("atf", "ATF Bank", "ATF", "Kazakhstan"),
    Bereke("bereke", "Bereke Bank", "B", "Kazakhstan"),
    Revolut("revolut", "Revolut", "R", "United Kingdom"),
    Wise("wise", "Wise", "W", "Belgium"),
    Cash("cash", "Cash", "₸", null);

    fun brandColors(darkTheme: Boolean): BrandColors = when (this) {
        Kaspi -> BrandColors(Color(0xFFE63946), Color(0xFFFFFFFF))
        Halyk -> BrandColors(Color(0xFF0EAA5B), Color(0xFFFFFFFF))
        Forte -> BrandColors(Color(0xFFF1A33A), Color(0xFF1A1A1A))
        Jusan -> if (darkTheme) {
            BrandColors(Color(0xFF1F4F8B), Color(0xFFFFFFFF))
        } else {
            BrandColors(Color(0xFF0F3460), Color(0xFFFFFFFF))
        }
        Atf -> if (darkTheme) {
            BrandColors(Color(0xFF1B7DD6), Color(0xFFFFFFFF))
        } else {
            BrandColors(Color(0xFF005EB8), Color(0xFFFFFFFF))
        }
        Bereke -> if (darkTheme) {
            BrandColors(Color(0xFF7A718E), Color(0xFFFFFFFF))
        } else {
            BrandColors(Color(0xFF5C5470), Color(0xFFFFFFFF))
        }
        Revolut -> if (darkTheme) {
            BrandColors(Color(0xFF1A8AFB), Color(0xFFFFFFFF))
        } else {
            BrandColors(Color(0xFF0075EB), Color(0xFFFFFFFF))
        }
        Wise -> BrandColors(Color(0xFF9FE870), Color(0xFF163300))
        Cash -> if (darkTheme) {
            BrandColors(Color(0xFF5A8C66), Color(0xFFFBF8F2))
        } else {
            BrandColors(Color(0xFF3D7A4A), Color(0xFFFBF8F2))
        }
    }
}

@Immutable
data class BrandColors(
    val bg: Color,
    val fg: Color,
)
