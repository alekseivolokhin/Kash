package com.volokhinaleksey.kash.navigation.onboarding

import com.arkivanov.decompose.ComponentContext
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingEvent
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingStep
import com.volokhinaleksey.kash.presentation.onboarding.OnboardingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class OnboardingComponent(
    componentContext: ComponentContext,
    private val onFinished: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            OnboardingEvent.GetStartedClicked -> _uiState.update { it.copy(step = OnboardingStep.Terms) }
            OnboardingEvent.LoginClicked -> Unit
            OnboardingEvent.BackClicked -> goBack()
            OnboardingEvent.ContinueClicked -> {
                val state = _uiState.value
                if (state.step == OnboardingStep.Terms && state.canContinueFromTerms) {
                    _uiState.update { it.copy(step = OnboardingStep.Profile) }
                }
            }
            OnboardingEvent.StartTrackingClicked -> onFinished()
            OnboardingEvent.ToggleTerms -> _uiState.update { it.copy(termsAccepted = !it.termsAccepted) }
            OnboardingEvent.TogglePrivacy -> _uiState.update { it.copy(privacyAccepted = !it.privacyAccepted) }
            OnboardingEvent.ToggleCrashReports -> _uiState.update { it.copy(crashReportsEnabled = !it.crashReportsEnabled) }
            is OnboardingEvent.DisplayNameChanged -> _uiState.update { it.copy(displayName = event.text) }
            is OnboardingEvent.AvatarSelected -> _uiState.update { it.copy(selectedAvatarIndex = event.index) }
            is OnboardingEvent.CurrencySelected -> _uiState.update { it.copy(selectedCurrency = event.currency) }
        }
    }

    private fun goBack() {
        _uiState.update {
            when (it.step) {
                OnboardingStep.Splash -> it
                OnboardingStep.Terms -> it.copy(step = OnboardingStep.Splash)
                OnboardingStep.Profile -> it.copy(step = OnboardingStep.Terms)
            }
        }
    }
}
