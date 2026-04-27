package com.volokhinaleksey.kash.navigation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.theme.InterTightFontFamily
import com.volokhinaleksey.kash.theme.Kash

internal data class AvatarSwatch(val bg: Color, val ink: Color)

internal val OnboardingAvatarSwatches = listOf(
    AvatarSwatch(Color(0xFF1F3D2C), Color(0xFFFBF8F2)),
    AvatarSwatch(Color(0xFF7A4A1E), Color(0xFFF4E9DC)),
    AvatarSwatch(Color(0xFF2C4A66), Color(0xFFE2E9F0)),
    AvatarSwatch(Color(0xFF5C3A66), Color(0xFFEFE4F0)),
    AvatarSwatch(Color(0xFF7A3A30), Color(0xFFF4DFDC)),
    AvatarSwatch(Color(0xFF5A4E2A), Color(0xFFEAE6DC)),
)

internal fun avatarSwatchAt(index: Int): AvatarSwatch =
    OnboardingAvatarSwatches[index.coerceIn(0, OnboardingAvatarSwatches.lastIndex)]

@Composable
internal fun ProfileAvatarSection(
    selectedIndex: Int,
    letter: String,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val swatch = avatarSwatchAt(selectedIndex)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SelectedAvatar(swatch = swatch, letter = letter)
        ProfileAvatarPicker(
            selectedIndex = selectedIndex,
            letter = letter,
            onSelect = onSelect,
        )
    }
}

@Composable
private fun SelectedAvatar(swatch: AvatarSwatch, letter: String) {
    Box(
        modifier = Modifier
            .size(84.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(swatch.bg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = letter,
            color = swatch.ink,
            fontFamily = InterTightFontFamily(),
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-1.2).sp,
        )
    }
}

@Composable
private fun ProfileAvatarPicker(
    selectedIndex: Int,
    letter: String,
    onSelect: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OnboardingAvatarSwatches.forEachIndexed { index, swatch ->
            AvatarPickerItem(
                swatch = swatch,
                letter = letter,
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
            )
        }
        AvatarPlaceholder()
    }
}

@Composable
private fun AvatarPickerItem(
    swatch: AvatarSwatch,
    letter: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .border(2.dp, Kash.colors.accent, RoundedCornerShape(13.dp)),
            )
        }
        Box(
            modifier = Modifier
                .padding(if (selected) 4.dp else 0.dp)
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(swatch.bg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = letter,
                color = swatch.ink,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun AvatarPlaceholder() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(if (Kash.colors.isDark) Kash.colors.cardAlt else Kash.colors.chipBg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "+",
            color = Kash.colors.sub,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
