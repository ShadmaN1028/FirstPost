package com.shadman.firstpost.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue

private val FEED_TABS = listOf("All", "Following", "Video")
private const val BADGE_MAX = 9

// "Feed" header + the All/Following/Video row. Only "All" has real content
// behind it — the other two are shown for layout fidelity, per Section 6.
@Composable
fun FeedTopBar(unreadNotificationCount: Int, onNotificationsClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Feed", style = MaterialTheme.typography.headlineLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Menu, contentDescription = null, tint = TextPrimary, modifier = Modifier.height(24.dp))
                Spacer(Modifier.width(20.dp))
                Icon(Icons.Filled.Search, contentDescription = "Search", tint = TextPrimary, modifier = Modifier.height(24.dp))
                Spacer(Modifier.width(20.dp))
                Box(modifier = Modifier.clickable(onClick = onNotificationsClick)) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = TextPrimary,
                        modifier = Modifier.height(24.dp),
                    )
                    if (unreadNotificationCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE03131)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (unreadNotificationCount > BADGE_MAX) "$BADGE_MAX+" else "$unreadNotificationCount",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 9.sp),
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            FEED_TABS.forEachIndexed { index, label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (index == 0) TextPrimary else TextMuted,
                )
            }
        }
    }
}
