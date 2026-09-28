package com.shadman.firstpost.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Male
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val INTERESTED_IN_OPTIONS = listOf(
    SelectableOption("Male", Icons.Filled.Male),
    SelectableOption("Female", Icons.Filled.Female),
    SelectableOption("Both", Icons.Filled.Groups),
    SelectableOption("Prefer not to say", Icons.Filled.Block),
)

// "07/08 Interested in". The last of the seven original steps, so its
// button always reads "Done" and moves on to the new Say hello step.
@Composable
fun Step07InterestedInScreen(
    onboardingViewModel: OnboardingViewModel,
    onBackClick: () -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val draft by onboardingViewModel.draft.collectAsState()

    OnboardingStepScaffold(
        step = 7,
        title = "Interested in",
        subtitle = "Let others know who you'd like to connect with. You can skip this.",
        onBackClick = onBackClick,
        modifier = modifier,
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                INTERESTED_IN_OPTIONS.forEach { option ->
                    SelectableOptionCard(
                        option = option,
                        selected = draft.interestedIn == option.label,
                        onClick = { onboardingViewModel.setInterestedIn(option.label) },
                    )
                }
            }
        },
        bottomBar = {
            OnboardingBottomButton(text = "Done", filled = true, onClick = onDoneClick)
        },
    )
}
