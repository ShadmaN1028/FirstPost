package com.shadman.firstpost.circle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.data.User
import com.shadman.firstpost.onboarding.AvatarPhoto
import com.shadman.firstpost.onboarding.OnboardingBottomButton
import com.shadman.firstpost.onboarding.initialsFrom
import com.shadman.firstpost.ui.theme.NavyBackground
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlue

// "Build your circle" — 5 invented people, pre-ticked (matches the real
// app), each with a decorative premium-style badge. No real friend system:
// selecting Continue just writes the chosen users into AppRepository.
@Composable
fun CircleScreen(onContinueClick: () -> Unit, modifier: Modifier = Modifier) {
    val candidates = AppRepository.circleCandidates
    var selectedIds by remember { mutableStateOf(candidates.map { it.id }.toSet()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("Build your circle", style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = "Add ${candidates.size} people to get started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                )
            }
            Text(
                text = "Skip for now",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                modifier = Modifier.clickable(onClick = onContinueClick),
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(candidates) { user ->
                val selected = user.id in selectedIds
                CircleCandidateRow(
                    user = user,
                    selected = selected,
                    onToggle = {
                        selectedIds = if (selected) selectedIds - user.id else selectedIds + user.id
                    },
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            OnboardingBottomButton(
                text = "Continue",
                filled = true,
                onClick = {
                    AppRepository.setFriends(candidates.filter { it.id in selectedIds })
                    onContinueClick()
                },
            )
        }
    }
}

@Composable
private fun CircleCandidateRow(user: User, selected: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) NavySurface else NavySurface.copy(alpha = 0.5f))
            .clickable(onClick = onToggle)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AvatarPhoto(photoUri = user.photoUri, initials = initialsFrom(user.name), size = 48.dp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(user.name, style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.width(6.dp))
                Text("💎", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
            )
        }
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(if (selected) VmpBlue else Color.Transparent)
                .border(1.dp, if (selected) VmpBlue else OutlineNavy, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
