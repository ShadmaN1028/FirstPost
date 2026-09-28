package com.shadman.firstpost.onboarding

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.ui.components.EarnedPremiumDialog
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue
import com.shadman.firstpost.ui.theme.VmpBlueContainer

// "08/08 Say hello" — the core of the demo. A live preview of the post the
// user is about to make (the caption is edited right inside it), and a few
// one-tap suggestions. All the state lives in OnboardingViewModel; this
// screen only renders it and reports taps.
@Composable
fun Step08SayHelloScreen(
    onboardingViewModel: OnboardingViewModel,
    onPostClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val draft by onboardingViewModel.draft.collectAsState()
    val firstName = remember(draft.name) { firstNameOf(draft.name) }
    val suggestions = remember(firstName) { captionSuggestions(firstName) }
    var showSkipDialog by remember { mutableStateOf(false) }
    var showEarnedPremiumDialog by remember { mutableStateOf(false) }

    LaunchedEffect(firstName) {
        onboardingViewModel.prefillCaptionIfBlank(firstName)
    }
    LaunchedEffect(Unit) {
        onboardingViewModel.finalizeProfile()
    }

    // Post is called from two places (the main button and the Skip dialog's
    // "Post & get Premium"), so the earned-Premium check lives in one spot.
    fun postAndContinue() {
        onboardingViewModel.postFirstHello()
        if (AppRepository.celebrationPending.value) {
            AppRepository.consumeCelebration()
            showEarnedPremiumDialog = true
        } else {
            onPostClick()
        }
    }

    OnboardingStepScaffold(
        step = 8,
        title = "Say hello",
        subtitle = "Your first post helps people find you.",
        onBackClick = null,
        modifier = modifier,
        content = {
            Column {
                SayHelloPreviewCard(
                    authorName = draft.name.ifBlank { "You" },
                    authorInitials = initialsFrom(draft.name),
                    photoUri = draft.photoUri,
                    caption = draft.caption,
                    onCaptionChange = onboardingViewModel::setCaption,
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    suggestions.forEach { suggestion ->
                        val selected = suggestion == draft.caption
                        SuggestionChip(
                            onClick = { onboardingViewModel.setCaption(suggestion) },
                            label = {
                                Text(
                                    text = suggestion,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(max = 220.dp),
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (selected) VmpBlueContainer else NavySurface,
                                labelColor = if (selected) VmpBlue else TextPrimary,
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                enabled = true,
                                borderColor = if (selected) VmpBlue else OutlineNavy,
                            ),
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column {
                OnboardingBottomButton(
                    text = "Post",
                    filled = true,
                    onClick = ::postAndContinue,
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { showSkipDialog = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Skip for now", style = MaterialTheme.typography.labelLarge)
                }
            }
        },
    )

    if (showSkipDialog) {
        SkipFirstPostDialog(
            onPostInstead = {
                showSkipDialog = false
                postAndContinue()
            },
            onConfirmSkip = {
                showSkipDialog = false
                onSkipClick()
            },
            onDismiss = { showSkipDialog = false },
        )
    }

    if (showEarnedPremiumDialog) {
        EarnedPremiumDialog(
            onDismiss = {
                showEarnedPremiumDialog = false
                onPostClick()
            },
        )
    }
}
