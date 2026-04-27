package com.volokhinaleksey.kash.navigation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.feedback.KashPageDots
import com.volokhinaleksey.kash.designsystem.feedback.KashScreenSubtitle
import com.volokhinaleksey.kash.designsystem.feedback.KashScreenTitle
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import com.volokhinaleksey.kash.designsystem.layout.KashBottomCtaArea
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingCurrency
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingUiState
import com.volokhinaleksey.kash.theme.InterTightFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.onb_base_currency_label
import kash.composeapp.generated.resources.onb_display_name_hint
import kash.composeapp.generated.resources.onb_display_name_label
import kash.composeapp.generated.resources.onb_display_name_placeholder
import kash.composeapp.generated.resources.onb_profile_subtitle
import kash.composeapp.generated.resources.onb_profile_title
import kash.composeapp.generated.resources.onb_start_tracking
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private val CurrencyOptions = listOf(
    OnboardingCurrency.KZT,
    OnboardingCurrency.USD,
    OnboardingCurrency.EUR,
    OnboardingCurrency.RUB,
)

@Composable
internal fun ProfileContent(
    state: OnboardingUiState,
    onBackClick: () -> Unit,
    onAvatarSelect: (Int) -> Unit,
    onDisplayNameChange: (String) -> Unit,
    onCurrencySelect: (OnboardingCurrency) -> Unit,
    onStartTrackingClick: () -> Unit,
) {
    val placeholder = stringResource(Res.string.onb_display_name_placeholder)
    val avatarLetter = (state.displayName.firstOrNull() ?: placeholder.firstOrNull() ?: 'A')
        .uppercase()

    Column(modifier = Modifier.fillMaxSize()) {
        KashBackTopBar(title = "", onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 30.dp),
        ) {
            OnboardingStepLabel(current = 2, total = 2)
            Spacer(Modifier.height(10.dp))
            KashScreenTitle(text = stringResource(Res.string.onb_profile_title))
            Spacer(Modifier.height(10.dp))
            KashScreenSubtitle(text = stringResource(Res.string.onb_profile_subtitle))

            Spacer(Modifier.height(24.dp))
            ProfileAvatarSection(
                selectedIndex = state.selectedAvatarIndex,
                letter = avatarLetter,
                onSelect = onAvatarSelect,
            )

            Spacer(Modifier.height(24.dp))
            DisplayNameSection(
                value = state.displayName,
                placeholder = placeholder,
                onValueChange = onDisplayNameChange,
            )

            Spacer(Modifier.height(22.dp))
            CurrencySection(
                selected = state.selectedCurrency,
                onSelect = onCurrencySelect,
            )
        }

        KashBottomCtaArea {
            KashButton(
                text = stringResource(Res.string.onb_start_tracking),
                onClick = onStartTrackingClick,
            )
            Spacer(Modifier.height(18.dp))
            KashPageDots(activeIndex = 2, total = 3)
        }
    }
}

@Composable
private fun DisplayNameSection(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    Column {
        KashSectionLabel(text = stringResource(Res.string.onb_display_name_label))
        Spacer(Modifier.height(8.dp))
        OnboardingNameField(
            value = value,
            placeholder = placeholder,
            onValueChange = onValueChange,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(Res.string.onb_display_name_hint),
            color = Kash.colors.fade,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun OnboardingNameField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Kash.colors.card)
            .border(1.5.dp, Kash.colors.accent, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(Kash.colors.accent),
            textStyle = TextStyle(
                color = Kash.colors.text,
                fontFamily = InterTightFontFamily(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Kash.colors.fade,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                innerField()
            },
        )
    }
}

@Composable
private fun CurrencySection(
    selected: OnboardingCurrency,
    onSelect: (OnboardingCurrency) -> Unit,
) {
    Column {
        KashSectionLabel(text = stringResource(Res.string.onb_base_currency_label))
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CurrencyOptions.forEach { currency ->
                CurrencyOption(
                    currency = currency,
                    selected = currency == selected,
                    onClick = { onSelect(currency) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun CurrencyOption(
    currency: OnboardingCurrency,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) Kash.colors.accent else Kash.colors.card
    val symbolColor = if (selected) Kash.colors.accentInk else Kash.colors.text
    val codeAlpha = if (selected) 0.85f else 0.6f
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .let {
                if (selected) it
                else it.border(1.dp, Kash.colors.line, RoundedCornerShape(12.dp))
            }
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = currency.symbol,
            color = symbolColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.4).sp,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = currency.code,
            color = symbolColor.copy(alpha = codeAlpha),
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview
@Composable
private fun ProfileContentPreview() {
    KashTheme {
        ProfileContent(
            state = OnboardingUiState(
                displayName = "Alex",
                selectedAvatarIndex = 2,
                selectedCurrency = OnboardingCurrency.KZT,
            ),
            onBackClick = {},
            onAvatarSelect = {},
            onDisplayNameChange = {},
            onCurrencySelect = {},
            onStartTrackingClick = {},
        )
    }
}
