package com.volokhinaleksey.kash.navigation.onboarding

import androidx.compose.runtime.Composable
import com.volokhinaleksey.kash.designsystem.feedback.KashSectionLabel
import kash.composeapp.generated.resources.Res
import kash.composeapp.generated.resources.onb_step_format
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OnboardingStepLabel(current: Int, total: Int) {
    KashSectionLabel(text = stringResource(Res.string.onb_step_format, current, total))
}
