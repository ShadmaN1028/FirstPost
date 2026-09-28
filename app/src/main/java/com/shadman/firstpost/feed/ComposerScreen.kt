package com.shadman.firstpost.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.onboarding.AvatarPhoto
import com.shadman.firstpost.onboarding.OnboardingTextField
import com.shadman.firstpost.onboarding.captionSuggestions
import com.shadman.firstpost.onboarding.firstNameOf
import com.shadman.firstpost.onboarding.initialsFrom
import com.shadman.firstpost.ui.components.EarnedPremiumDialog
import com.shadman.firstpost.ui.theme.NavyBackground
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue

// "New post", opened by tapping the feed's composer bar. Starts empty — the
// placeholder and the suggestion chips are personalised, but nothing is
// pre-filled the way Say hello's caption was.
@Composable
fun ComposerScreen(onBackClick: () -> Unit, onPosted: () -> Unit, modifier: Modifier = Modifier) {
    val currentUser by AppRepository.currentUser.collectAsState()
    var text by remember { mutableStateOf("") }
    var showEarnedPremiumDialog by remember { mutableStateOf(false) }
    val firstName = remember(currentUser?.name) { firstNameOf(currentUser?.name.orEmpty()) }
    val suggestions = remember(firstName) { captionSuggestions(firstName) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
            Text("New post", style = MaterialTheme.typography.titleMedium)
            TextButton(
                enabled = text.isNotBlank(),
                onClick = {
                    AppRepository.addPost(text = text)
                    if (AppRepository.celebrationPending.value) {
                        AppRepository.consumeCelebration()
                        showEarnedPremiumDialog = true
                    } else {
                        onPosted()
                    }
                },
            ) {
                Text("Post", color = if (text.isNotBlank()) VmpBlue else TextMuted)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarPhoto(photoUri = currentUser?.photoUri, initials = initialsFrom(currentUser?.name.orEmpty()), size = 44.dp)
            Spacer(Modifier.width(12.dp))
            Text(currentUser?.name.orEmpty(), style = MaterialTheme.typography.titleSmall)
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            OnboardingTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "What's on your mind, $firstName?",
                singleLine = false,
                minLines = 3,
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                suggestions.forEach { suggestion ->
                    SuggestionChip(
                        onClick = { text = suggestion },
                        label = {
                            Text(
                                text = suggestion,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.widthIn(max = 220.dp),
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = NavySurface,
                            labelColor = TextPrimary,
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(enabled = true, borderColor = OutlineNavy),
                    )
                }
            }
        }
    }

    if (showEarnedPremiumDialog) {
        EarnedPremiumDialog(
            onDismiss = {
                showEarnedPremiumDialog = false
                onPosted()
            },
        )
    }
}
