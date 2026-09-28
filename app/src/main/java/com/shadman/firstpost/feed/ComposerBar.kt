package com.shadman.firstpost.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.User
import com.shadman.firstpost.onboarding.AvatarPhoto
import com.shadman.firstpost.onboarding.initialsFrom
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.TextMuted

// The "Create a post" bar at the top of the feed. Tapping anywhere on it
// opens the composer — the avatar also opens the user's own profile,
// matching the real app where the avatar is its own tap target.
@Composable
fun ComposerBar(
    currentUser: User?,
    onAvatarClick: () -> Unit,
    onComposerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.clickable(onClick = onAvatarClick)) {
            AvatarPhoto(
                photoUri = currentUser?.photoUri,
                initials = initialsFrom(currentUser?.name.orEmpty()),
                size = 40.dp,
            )
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .weight(1f, fill = true)
                .height(40.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(NavySurface)
                .clickable(onClick = onComposerClick)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text("Create a post", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        }
    }
}
