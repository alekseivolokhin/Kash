package com.volokhinaleksey.kash.navigation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingCurrency
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingEvent
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingStep
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingUiState
import com.volokhinaleksey.kash.theme.Kash
import com.volokhinaleksey.kash.theme.KashTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OnboardingScreen(
    component: OnboardingComponent,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val state by component.uiState.collectAsState()
    val callbacks = remember(component) { OnboardingCallbacks.from(component::onEvent) }
    OnboardingContent(
        state = state,
        callbacks = callbacks,
        contentPadding = contentPadding,
    )
}

internal data class OnboardingCallbacks(
    val onBackClick: () -> Unit,
    val onGetStartedClick: () -> Unit,
    val onLoginClick: () -> Unit,
    val onToggleTerms: () -> Unit,
    val onTogglePrivacy: () -> Unit,
    val onToggleCrashReports: () -> Unit,
    val onContinueClick: () -> Unit,
    val onAvatarSelect: (Int) -> Unit,
    val onDisplayNameChange: (String) -> Unit,
    val onCurrencySelect: (OnboardingCurrency) -> Unit,
    val onStartTrackingClick: () -> Unit,
) {
    companion object {
        fun from(onEvent: (OnboardingEvent) -> Unit): OnboardingCallbacks = OnboardingCallbacks(
            onBackClick = { onEvent(OnboardingEvent.BackClicked) },
            onGetStartedClick = { onEvent(OnboardingEvent.GetStartedClicked) },
            onLoginClick = { onEvent(OnboardingEvent.LoginClicked) },
            onToggleTerms = { onEvent(OnboardingEvent.ToggleTerms) },
            onTogglePrivacy = { onEvent(OnboardingEvent.TogglePrivacy) },
            onToggleCrashReports = { onEvent(OnboardingEvent.ToggleCrashReports) },
            onContinueClick = { onEvent(OnboardingEvent.ContinueClicked) },
            onAvatarSelect = { onEvent(OnboardingEvent.AvatarSelected(it)) },
            onDisplayNameChange = { onEvent(OnboardingEvent.DisplayNameChanged(it)) },
            onCurrencySelect = { onEvent(OnboardingEvent.CurrencySelected(it)) },
            onStartTrackingClick = { onEvent(OnboardingEvent.StartTrackingClicked) },
        )

        val NoOp: OnboardingCallbacks = from {}
    }
}

@Composable
private fun OnboardingContent(
    state: OnboardingUiState,
    callbacks: OnboardingCallbacks,
    contentPadding: PaddingValues = PaddingValues(),
) {
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
                val direction = if (forward) SlideDirection.Left else SlideDirection.Right
                val slideAnim = tween<IntOffset>(durationMillis = 280)
                val fadeAnim = tween<Float>(durationMillis = 220)
                (slideIntoContainer(towards = direction, animationSpec = slideAnim) + fadeIn(fadeAnim))
                    .togetherWith(slideOutOfContainer(towards = direction, animationSpec = slideAnim) + fadeOut(fadeAnim))
            },
            label = "onboardingStep",
        ) { step ->
            when (step) {
                OnboardingStep.Splash -> SplashContent(
                    onGetStartedClick = callbacks.onGetStartedClick,
                    onLoginClick = callbacks.onLoginClick,
                )
                OnboardingStep.Terms -> TermsContent(
                    state = state,
                    onBackClick = callbacks.onBackClick,
                    onToggleTerms = callbacks.onToggleTerms,
                    onTogglePrivacy = callbacks.onTogglePrivacy,
                    onToggleCrashReports = callbacks.onToggleCrashReports,
                    onContinueClick = callbacks.onContinueClick,
                )
                OnboardingStep.Profile -> ProfileContent(
                    state = state,
                    onBackClick = callbacks.onBackClick,
                    onAvatarSelect = callbacks.onAvatarSelect,
                    onDisplayNameChange = callbacks.onDisplayNameChange,
                    onCurrencySelect = callbacks.onCurrencySelect,
                    onStartTrackingClick = callbacks.onStartTrackingClick,
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingContentPreview() {
    KashTheme {
        OnboardingContent(
            state = OnboardingUiState(step = OnboardingStep.Splash),
            callbacks = OnboardingCallbacks.NoOp,
        )
    }
}
