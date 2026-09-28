package com.shadman.firstpost.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlueContainer
import com.shadman.firstpost.ui.theme.SuccessGreen

// "03/08 Choose a username". Suggests one from the name the first time this
// step is seen (see OnboardingViewModel.suggestUsernameIfBlank), then stays
// out of the way — the field is always editable after that.
@Composable
fun Step03UsernameScreen(
    onboardingViewModel: OnboardingViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val draft by onboardingViewModel.draft.collectAsState()

    LaunchedEffect(Unit) {
        onboardingViewModel.suggestUsernameIfBlank()
    }

    OnboardingStepScaffold(
        step = 3,
        title = "Choose a username",
        subtitle = "This is your unique handle on VMP.",
        onBackClick = onBackClick,
        modifier = modifier,
        content = {
            Column {
                OnboardingTextField(
                    value = draft.username,
                    onValueChange = { onboardingViewModel.setUsername(it.lowercase()) },
                    prefix = { Text("@", color = TextMuted) },
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (draft.isUsernameValid) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.height(16.dp).width(16.dp),
                        )
                        Spacer(Modifier.width(6.dp))
                    }
                    Text(
                        text = "3-30 characters · lowercase letters and numbers",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (draft.isUsernameValid) SuccessGreen else TextMuted,
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(VmpBlueContainer, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.height(18.dp).width(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "You can change your username again 14 days after setting it.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted,
                    )
                }
            }
        },
        bottomBar = {
            OnboardingBottomButton(text = "Next", filled = true, enabled = draft.isUsernameValid, onClick = onNextClick)
        },
    )
}
