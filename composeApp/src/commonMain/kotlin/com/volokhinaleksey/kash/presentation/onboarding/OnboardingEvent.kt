package com.volokhinaleksey.kash.presentation.onboarding

sealed interface OnboardingEvent {
    data object GetStartedClicked : OnboardingEvent
    data object LoginClicked : OnboardingEvent
    data object BackClicked : OnboardingEvent
    data object ContinueClicked : OnboardingEvent
    data object StartTrackingClicked : OnboardingEvent

    data object ToggleTerms : OnboardingEvent
    data object TogglePrivacy : OnboardingEvent
    data object ToggleCrashReports : OnboardingEvent

    data class DisplayNameChanged(val text: String) : OnboardingEvent
    data class AvatarSelected(val index: Int) : OnboardingEvent
    data class CurrencySelected(val currency: OnboardingCurrency) : OnboardingEvent
}
