package com.shadman.firstpost.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.TextMuted

// "02/08 What's your name?". Pre-filled from the mock Google account
// (OnboardingViewModel.prefillName, called right after sign-in) and editable.
@Composable
fun Step02NameScreen(
    onboardingViewModel: OnboardingViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val draft by onboardingViewModel.draft.collectAsState()

    OnboardingStepScaffold(
        step = 2,
        title = "What's your name?",
        subtitle = "This is the name people will see on your profile.",
        onBackClick = onBackClick,
        modifier = modifier,
        content = {
            Column {
                OnboardingTextField(
                    value = draft.name,
                    onValueChange = onboardingViewModel::setName,
                    placeholder = "Your name",
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Tip: use the name friends know you by.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                )
            }
        },
        bottomBar = {
            OnboardingBottomButton(text = "Next", filled = true, enabled = draft.isNameValid, onClick = onNextClick)
        },
    )
}
