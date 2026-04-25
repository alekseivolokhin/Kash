package com.volokhinaleksey.kash.navigation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volokhinaleksey.kash.components.BackTopBar
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingCurrency
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingEvent
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingStep
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingUiState
import com.volokhinaleksey.kash.theme.InterTightFontFamily
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashColors
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.onb_base_currency_label
import kash.composeapp.generated.resources.onb_check_crash_sub
import kash.composeapp.generated.resources.onb_check_crash_title
import kash.composeapp.generated.resources.onb_check_privacy_sub
import kash.composeapp.generated.resources.onb_check_privacy_title
import kash.composeapp.generated.resources.onb_check_terms_sub
import kash.composeapp.generated.resources.onb_check_terms_title
import kash.composeapp.generated.resources.onb_continue
import kash.composeapp.generated.resources.onb_currency_eur_name
import kash.composeapp.generated.resources.onb_currency_kzt_name
import kash.composeapp.generated.resources.onb_currency_rub_name
import kash.composeapp.generated.resources.onb_currency_usd_name
import kash.composeapp.generated.resources.onb_display_name_hint
import kash.composeapp.generated.resources.onb_display_name_label
import kash.composeapp.generated.resources.onb_display_name_placeholder
import kash.composeapp.generated.resources.onb_feature_categories_sub
import kash.composeapp.generated.resources.onb_feature_categories_title
import kash.composeapp.generated.resources.onb_feature_imports_sub
import kash.composeapp.generated.resources.onb_feature_imports_title
import kash.composeapp.generated.resources.onb_feature_local_sub
import kash.composeapp.generated.resources.onb_feature_local_title
import kash.composeapp.generated.resources.onb_get_started
import kash.composeapp.generated.resources.onb_login_hint
import kash.composeapp.generated.resources.onb_logo_letter
import kash.composeapp.generated.resources.onb_profile_subtitle
import kash.composeapp.generated.resources.onb_profile_title
import kash.composeapp.generated.resources.onb_splash_subtitle
import kash.composeapp.generated.resources.onb_splash_title
import kash.composeapp.generated.resources.onb_start_tracking
import kash.composeapp.generated.resources.onb_step_format
import kash.composeapp.generated.resources.onb_terms_banner
import kash.composeapp.generated.resources.onb_terms_subtitle
import kash.composeapp.generated.resources.onb_terms_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreen(
    component: OnboardingComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    val onEvent = remember(component) { component::onEvent }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Kash.colors.bg)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
            ),
    ) {
        AnimatedContent(
            targetState = state.step,
            transitionSpec = {
                val forward = targetState.ordinal > initialState.ordinal
                val enterDir = if (forward) SlideDirection.Left else SlideDirection.Right
                val exitDir = if (forward) SlideDirection.Left else SlideDirection.Right
                val anim = tween<IntOffset>(durationMillis = 280)
                val fadeAnim = tween<Float>(durationMillis = 220)
                (slideIntoContainer(towards = enterDir, animationSpec = anim) + fadeIn(fadeAnim))
                    .togetherWith(slideOutOfContainer(towards = exitDir, animationSpec = anim) + fadeOut(fadeAnim))
            },
            label = "onboardingStep",
        ) { step ->
            when (step) {
                OnboardingStep.Splash -> SplashContent(onEvent = onEvent)
                OnboardingStep.Terms -> TermsContent(state = state, onEvent = onEvent)
                OnboardingStep.Profile -> ProfileContent(state = state, onEvent = onEvent)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Splash
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SplashContent(onEvent: (OnboardingEvent) -> Unit) {
    val c = Kash.colors
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(Modifier.height(24.dp))
            LogoBadge(size = 96.dp, radius = 28.dp, fontSize = 48.sp, letterSpacing = (-2).sp)

            Spacer(Modifier.height(28.dp))
            Text(
                text = stringResource(Res.string.onb_splash_title),
                color = c.text,
                fontFamily = InterTightFontFamily(),
                fontSize = 38.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1.4).sp,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(Res.string.onb_splash_subtitle),
                color = c.sub,
                fontSize = 15.sp,
                lineHeight = 22.5.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 300.dp),
            )

            Spacer(Modifier.height(32.dp))
            Column(
                modifier = Modifier.widthIn(max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                FeatureRow(
                    title = stringResource(Res.string.onb_feature_imports_title),
                    sub = stringResource(Res.string.onb_feature_imports_sub),
                )
                FeatureRow(
                    title = stringResource(Res.string.onb_feature_categories_title),
                    sub = stringResource(Res.string.onb_feature_categories_sub),
                )
                FeatureRow(
                    title = stringResource(Res.string.onb_feature_local_title),
                    sub = stringResource(Res.string.onb_feature_local_sub),
                )
            }
            Spacer(Modifier.height(24.dp))
        }

        BottomCtaArea {
            CtaButton(
                text = stringResource(Res.string.onb_get_started),
                onClick = { onEvent(OnboardingEvent.GetStartedClicked) },
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.onb_login_hint),
                color = c.sub,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(OnboardingEvent.LoginClicked) }
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(18.dp))
            PageDots(activeIndex = 0)
        }
    }
}

@Composable
private fun FeatureRow(title: String, sub: String) {
    val c = Kash.colors
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(22.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(c.accentSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = c.accentSoftInk,
                modifier = Modifier.size(12.dp),
            )
        }
        Column {
            Text(
                text = title,
                color = c.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = sub,
                color = c.sub,
                fontSize = 12.5.sp,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Terms
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun TermsContent(
    state: OnboardingUiState,
    onEvent: (OnboardingEvent) -> Unit,
) {
    val c = Kash.colors
    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(
            title = "",
            onBackClick = { onEvent(OnboardingEvent.BackClicked) },
            showNotifications = false,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 30.dp),
        ) {
            StepLabel(current = 1, total = 2)
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.onb_terms_title),
                color = c.text,
                fontFamily = InterTightFontFamily(),
                fontSize = 30.sp,
                lineHeight = 33.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1.2).sp,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.onb_terms_subtitle),
                color = c.sub,
                fontSize = 14.sp,
                lineHeight = 21.sp,
            )

            Spacer(Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ConsentRow(
                    title = stringResource(Res.string.onb_check_terms_title),
                    sub = stringResource(Res.string.onb_check_terms_sub),
                    checked = state.termsAccepted,
                    onToggle = { onEvent(OnboardingEvent.ToggleTerms) },
                )
                ConsentRow(
                    title = stringResource(Res.string.onb_check_privacy_title),
                    sub = stringResource(Res.string.onb_check_privacy_sub),
                    checked = state.privacyAccepted,
                    onToggle = { onEvent(OnboardingEvent.TogglePrivacy) },
                )
                ConsentRow(
                    title = stringResource(Res.string.onb_check_crash_title),
                    sub = stringResource(Res.string.onb_check_crash_sub),
                    checked = state.crashReportsEnabled,
                    onToggle = { onEvent(OnboardingEvent.ToggleCrashReports) },
                )
            }

            Spacer(Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(c.accentSoft)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            ) {
                Text(
                    text = stringResource(Res.string.onb_terms_banner),
                    color = c.accentSoftInk,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                )
            }
        }

        BottomCtaArea {
            CtaButton(
                text = stringResource(Res.string.onb_continue),
                onClick = { onEvent(OnboardingEvent.ContinueClicked) },
                enabled = state.canContinueFromTerms,
            )
            Spacer(Modifier.height(18.dp))
            PageDots(activeIndex = 1)
        }
    }
}

@Composable
private fun ConsentRow(
    title: String,
    sub: String,
    checked: Boolean,
    onToggle: () -> Unit,
) {
    val c = Kash.colors
    val borderColor = if (checked) c.accent else c.line
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(c.card)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        ConsentCheckbox(checked = checked)
        Column(modifier = Modifier.padding(top = 1.dp)) {
            Text(
                text = title,
                color = c.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.1).sp,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = sub,
                color = c.sub,
                fontSize = 12.5.sp,
                lineHeight = 18.sp,
            )
        }
    }
}

@Composable
private fun ConsentCheckbox(checked: Boolean) {
    val c = Kash.colors
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(if (checked) c.accent else Color.Transparent)
            .then(
                if (checked) Modifier
                else Modifier.border(1.5.dp, c.lineStrong, RoundedCornerShape(7.dp))
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = c.accentInk,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Profile
// ─────────────────────────────────────────────────────────────────────────────

private data class AvatarSwatch(val bg: Color, val ink: Color)

private val AvatarSwatches = listOf(
    AvatarSwatch(Color(0xFF1F3D2C), Color(0xFFFBF8F2)),
    AvatarSwatch(Color(0xFF7A4A1E), Color(0xFFF4E9DC)),
    AvatarSwatch(Color(0xFF2C4A66), Color(0xFFE2E9F0)),
    AvatarSwatch(Color(0xFF5C3A66), Color(0xFFEFE4F0)),
    AvatarSwatch(Color(0xFF7A3A30), Color(0xFFF4DFDC)),
    AvatarSwatch(Color(0xFF5A4E2A), Color(0xFFEAE6DC)),
)

@Composable
private fun ProfileContent(
    state: OnboardingUiState,
    onEvent: (OnboardingEvent) -> Unit,
) {
    val c = Kash.colors
    val selectedSwatch = AvatarSwatches[state.selectedAvatarIndex.coerceIn(0, AvatarSwatches.lastIndex)]
    val placeholder = stringResource(Res.string.onb_display_name_placeholder)
    val avatarLetter = (state.displayName.firstOrNull() ?: placeholder.firstOrNull() ?: 'A')
        .uppercase()

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(
            title = "",
            onBackClick = { onEvent(OnboardingEvent.BackClicked) },
            showNotifications = false,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp, bottom = 30.dp),
        ) {
            StepLabel(current = 2, total = 2)
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.onb_profile_title),
                color = c.text,
                fontFamily = InterTightFontFamily(),
                fontSize = 30.sp,
                lineHeight = 33.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1.2).sp,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(Res.string.onb_profile_subtitle),
                color = c.sub,
                fontSize = 14.sp,
                lineHeight = 21.sp,
            )

            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(selectedSwatch.bg),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = avatarLetter,
                        color = selectedSwatch.ink,
                        fontFamily = InterTightFontFamily(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-1.2).sp,
                    )
                }
                AvatarPicker(
                    selectedIndex = state.selectedAvatarIndex,
                    letter = avatarLetter,
                    onSelect = { onEvent(OnboardingEvent.AvatarSelected(it)) },
                )
            }

            Spacer(Modifier.height(24.dp))
            FieldLabel(text = stringResource(Res.string.onb_display_name_label))
            Spacer(Modifier.height(8.dp))
            DisplayNameField(
                value = state.displayName,
                placeholder = placeholder,
                onValueChange = { onEvent(OnboardingEvent.DisplayNameChanged(it)) },
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(Res.string.onb_display_name_hint),
                color = c.fade,
                fontSize = 12.sp,
            )

            Spacer(Modifier.height(22.dp))
            FieldLabel(text = stringResource(Res.string.onb_base_currency_label))
            Spacer(Modifier.height(8.dp))
            CurrencyGrid(
                selected = state.selectedCurrency,
                onSelect = { onEvent(OnboardingEvent.CurrencySelected(it)) },
            )
        }

        BottomCtaArea {
            CtaButton(
                text = stringResource(Res.string.onb_start_tracking),
                onClick = { onEvent(OnboardingEvent.StartTrackingClicked) },
            )
            Spacer(Modifier.height(18.dp))
            PageDots(activeIndex = 2)
        }
    }
}

@Composable
private fun AvatarPicker(
    selectedIndex: Int,
    letter: String,
    onSelect: (Int) -> Unit,
) {
    val c = Kash.colors
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        AvatarSwatches.forEachIndexed { index, swatch ->
            val isSelected = index == selectedIndex
            val outerPadding = if (isSelected) 4.dp else 0.dp
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center,
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .border(2.dp, c.accent, RoundedCornerShape(13.dp)),
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(outerPadding)
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
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(if (c.isDark) c.cardAlt else c.chipBg),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "+",
                color = c.sub,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun DisplayNameField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    val c = Kash.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(c.card)
            .border(1.5.dp, c.accent, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(c.accent),
            textStyle = TextStyle(
                color = c.text,
                fontFamily = InterTightFontFamily(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = c.fade,
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
private fun CurrencyGrid(
    selected: OnboardingCurrency,
    onSelect: (OnboardingCurrency) -> Unit,
) {
    val items = listOf(
        OnboardingCurrency.KZT to stringResource(Res.string.onb_currency_kzt_name),
        OnboardingCurrency.USD to stringResource(Res.string.onb_currency_usd_name),
        OnboardingCurrency.EUR to stringResource(Res.string.onb_currency_eur_name),
        OnboardingCurrency.RUB to stringResource(Res.string.onb_currency_rub_name),
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { (currency, _) ->
            CurrencyCell(
                currency = currency,
                selected = currency == selected,
                onClick = { onSelect(currency) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CurrencyCell(
    currency: OnboardingCurrency,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = Kash.colors
    val bg = if (selected) c.accent else c.card
    val symbolColor = if (selected) c.accentInk else c.text
    val codeAlpha = if (selected) 0.85f else 0.6f
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .then(
                if (selected) Modifier
                else Modifier.border(1.dp, c.line, RoundedCornerShape(12.dp))
            )
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

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = Kash.colors.sub,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.2.sp,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Shared bits
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StepLabel(current: Int, total: Int) {
    Text(
        text = stringResource(Res.string.onb_step_format, current, total),
        color = Kash.colors.sub,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.4.sp,
    )
}

@Composable
private fun BottomCtaArea(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
    ) {
        content()
    }
}

@Composable
private fun CtaButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    val c = Kash.colors
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.4f,
        animationSpec = tween(durationMillis = 150),
        label = "ctaAlpha",
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clip(RoundedCornerShape(14.dp))
            .background(c.accent)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = c.accentInk,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.2).sp,
        )
    }
}

@Composable
private fun PageDots(activeIndex: Int, total: Int = 3) {
    val c = Kash.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        for (i in 0 until total) {
            val active = i == activeIndex
            val width by animateDpAsState(
                targetValue = if (active) 22.dp else 6.dp,
                animationSpec = tween(durationMillis = 180),
                label = "dotWidth",
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .width(width)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(if (active) c.accent else c.lineStrong),
            )
        }
    }
}

@Composable
private fun LogoBadge(
    size: androidx.compose.ui.unit.Dp,
    radius: androidx.compose.ui.unit.Dp,
    fontSize: androidx.compose.ui.unit.TextUnit,
    letterSpacing: androidx.compose.ui.unit.TextUnit,
) {
    val c: KashColors = Kash.colors
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(radius))
            .background(c.accent),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.onb_logo_letter),
            color = c.accentInk,
            fontFamily = InterTightFontFamily(),
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = letterSpacing,
        )
    }
}
