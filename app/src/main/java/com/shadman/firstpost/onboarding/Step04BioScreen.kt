package com.shadman.firstpost.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.TextMuted

private const val BIO_MAX_LENGTH = 500

// "04/08 Add a short bio". Skippable — the bottom button reads "Skip" while
// the field is empty and becomes "Next" the moment there's text, so there's
// no separate disabled/enabled state to reason about.
@Composable
fun Step04BioScreen(
    onboardingViewModel: OnboardingViewModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val draft by onboardingViewModel.draft.collectAsState()

    OnboardingStepScaffold(
        step = 4,
        title = "Add a short bio",
        subtitle = "Tell people a little about yourself.",
        onBackClick = onBackClick,
        modifier = modifier,
        content = {
            Column {
                OnboardingTextField(
                    value = draft.bio,
                    onValueChange = { if (it.length <= BIO_MAX_LENGTH) onboardingViewModel.setBio(it) },
                    placeholder = "Write something about yourself...",
                    singleLine = false,
                    minLines = 3,
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Keep it human", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    Text(
                        text = "${draft.bio.length} / $BIO_MAX_LENGTH",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted,
                    )
                }
            }
        },
        bottomBar = {
            OnboardingBottomButton(
                text = if (draft.bio.isBlank()) "Skip" else "Next",
                filled = draft.bio.isNotBlank(),
                onClick = onContinueClick,
            )
        },
    )
}
