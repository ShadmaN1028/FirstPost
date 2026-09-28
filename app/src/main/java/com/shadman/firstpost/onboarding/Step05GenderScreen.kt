package com.shadman.firstpost.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Transgender
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val GENDER_OPTIONS = listOf(
    SelectableOption("Male", Icons.Filled.Male),
    SelectableOption("Female", Icons.Filled.Female),
    SelectableOption("Other", Icons.Filled.Transgender),
    SelectableOption("Prefer not to say", Icons.Filled.Block),
)

// "05/08 Your gender". Skippable, single choice.
@Composable
fun Step05GenderScreen(
    onboardingViewModel: OnboardingViewModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val draft by onboardingViewModel.draft.collectAsState()

    OnboardingStepScaffold(
        step = 5,
        title = "Your gender",
        subtitle = "This helps people get to know you. You can skip this.",
        onBackClick = onBackClick,
        modifier = modifier,
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                GENDER_OPTIONS.forEach { option ->
                    SelectableOptionCard(
                        option = option,
                        selected = draft.gender == option.label,
                        onClick = { onboardingViewModel.setGender(option.label) },
                    )
                }
            }
        },
        bottomBar = {
            OnboardingBottomButton(
                text = if (draft.gender == null) "Skip" else "Next",
                filled = draft.gender != null,
                onClick = onContinueClick,
            )
        },
    )
}
