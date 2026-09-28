package com.shadman.firstpost.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.data.PremiumStatus
import com.shadman.firstpost.onboarding.AvatarPhoto
import com.shadman.firstpost.onboarding.initialsFrom
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.TextMuted

// The Settings tab: the user's own card, a Premium status row, and a
// clearly labelled demo-only section for testing the trial without waiting
// two real days for it to lapse.
@Composable
fun SettingsScreen(onOpenPaywall: () -> Unit, modifier: Modifier = Modifier) {
    val currentUser by AppRepository.currentUser.collectAsState()
    val status = AppRepository.premiumStatus(currentUser)

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 20.dp, bottom = 16.dp),
        )

        SettingsCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarPhoto(photoUri = currentUser?.photoUri, initials = initialsFrom(currentUser?.name.orEmpty()), size = 48.dp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(currentUser?.name.orEmpty(), style = MaterialTheme.typography.titleSmall)
                    if (!currentUser?.username.isNullOrBlank()) {
                        Text("@${currentUser?.username}", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        SettingsCard(onClick = onOpenPaywall) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("VMP Premium", style = MaterialTheme.typography.titleSmall)
                    Text(premiumStatusText(status), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                }
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
            }
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "Demo controls",
            style = MaterialTheme.typography.titleSmall,
            color = TextMuted,
            modifier = Modifier.padding(vertical = 12.dp),
        )
        OutlinedButton(
            onClick = {
                AppRepository.endTrialNow()
                onOpenPaywall()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("End Premium trial now")
        }
        Spacer(Modifier.height(24.dp))
    }
}

private fun premiumStatusText(status: PremiumStatus): String = when (status) {
    is PremiumStatus.None -> "Not subscribed"
    is PremiumStatus.Trial -> "Trial · ${status.daysLeft} day${if (status.daysLeft == 1) "" else "s"} left"
    is PremiumStatus.Expired -> "Trial ended"
}

@Composable
private fun SettingsCard(onClick: (() -> Unit)? = null, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(NavySurface)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
    ) {
        content()
    }
}
