package com.volokhinaleksey.kash.presentation.onboarding

enum class OnboardingStep { Splash, Terms, Profile }

enum class OnboardingCurrency(val code: String, val symbol: String) {
    KZT("KZT", "₸"),
    USD("USD", "$"),
    EUR("EUR", "€"),
    RUB("RUB", "₽"),
}

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.Splash,
    val termsAccepted: Boolean = false,
    val privacyAccepted: Boolean = false,
    val crashReportsEnabled: Boolean = false,
    val displayName: String = "",
    val selectedAvatarIndex: Int = 0,
    val selectedCurrency: OnboardingCurrency = OnboardingCurrency.KZT,
) {
    val canContinueFromTerms: Boolean get() = termsAccepted && privacyAccepted
}
