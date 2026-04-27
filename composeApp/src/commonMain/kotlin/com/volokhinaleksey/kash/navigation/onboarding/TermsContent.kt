package com.volokhinaleksey.kash.navigation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.volokhinaleksey.kash.designsystem.button.KashButton
import com.volokhinaleksey.kash.designsystem.feedback.KashHintBanner
import com.volokhinaleksey.kash.designsystem.feedback.KashPageDots
import com.volokhinaleksey.kash.designsystem.feedback.KashScreenSubtitle
import com.volokhinaleksey.kash.designsystem.feedback.KashScreenTitle
import com.volokhinaleksey.kash.designsystem.field.KashCheckRow
import com.volokhinaleksey.kash.designsystem.layout.KashBottomCtaArea
import com.volokhinaleksey.kash.designsystem.topbar.KashBackTopBar
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingUiState
import com.volokhinaleksey.kash.theme.KashTheme
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.onb_check_crash_sub
import kash.composeapp.generated.resources.onb_check_crash_title
import kash.composeapp.generated.resources.onb_check_privacy_sub
import kash.composeapp.generated.resources.onb_check_privacy_title
import kash.composeapp.generated.resources.onb_check_terms_sub
import kash.composeapp.generated.resources.onb_check_terms_title
import kash.composeapp.generated.resources.onb_continue
import kash.composeapp.generated.resources.onb_terms_banner
import kash.composeapp.generated.resources.onb_terms_subtitle
import kash.composeapp.generated.resources.onb_terms_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TermsContent(
    state: OnboardingUiState,
    onBackClick: () -> Unit,
    onToggleTerms: () -> Unit,
    onTogglePrivacy: () -> Unit,
    onToggleCrashReports: () -> Unit,
    onContinueClick: () -> Unit,
) {
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
            OnboardingStepLabel(current = 1, total = 2)
            Spacer(Modifier.height(10.dp))
            KashScreenTitle(text = stringResource(Res.string.onb_terms_title))
            Spacer(Modifier.height(10.dp))
            KashScreenSubtitle(text = stringResource(Res.string.onb_terms_subtitle))

            Spacer(Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                KashCheckRow(
                    title = stringResource(Res.string.onb_check_terms_title),
                    subtitle = stringResource(Res.string.onb_check_terms_sub),
                    checked = state.termsAccepted,
                    onToggle = onToggleTerms,
                )
                KashCheckRow(
                    title = stringResource(Res.string.onb_check_privacy_title),
                    subtitle = stringResource(Res.string.onb_check_privacy_sub),
                    checked = state.privacyAccepted,
                    onToggle = onTogglePrivacy,
                )
                KashCheckRow(
                    title = stringResource(Res.string.onb_check_crash_title),
                    subtitle = stringResource(Res.string.onb_check_crash_sub),
                    checked = state.crashReportsEnabled,
                    onToggle = onToggleCrashReports,
                )
            }

            Spacer(Modifier.height(18.dp))
            KashHintBanner(text = stringResource(Res.string.onb_terms_banner))
        }

        KashBottomCtaArea {
            KashButton(
                text = stringResource(Res.string.onb_continue),
                onClick = onContinueClick,
                enabled = state.canContinueFromTerms,
            )
            Spacer(Modifier.height(18.dp))
            KashPageDots(activeIndex = 1, total = 3)
        }
    }
}

@Preview
@Composable
private fun TermsContentPreview() {
    KashTheme {
        TermsContent(
            state = OnboardingUiState(termsAccepted = true, privacyAccepted = false),
            onBackClick = {},
            onToggleTerms = {},
            onTogglePrivacy = {},
            onToggleCrashReports = {},
            onContinueClick = {},
        )
    }
}
