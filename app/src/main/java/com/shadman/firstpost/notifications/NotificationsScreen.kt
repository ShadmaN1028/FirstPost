package com.shadman.firstpost.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.AppNotification
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.data.NotificationType
import com.shadman.firstpost.data.relativeTimeText
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlue
import com.shadman.firstpost.ui.theme.VmpBlueContainer

// The bell icon's screen. Real reaction/comment notifications now that
// Section 7 built ReactionScheduler; opening this clears the unread badge.
@Composable
fun NotificationsScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    val notifications by AppRepository.notifications.collectAsState()

    LaunchedEffect(Unit) {
        AppRepository.markAllNotificationsRead()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Notifications", style = MaterialTheme.typography.titleMedium)
        }

        if (notifications.isEmpty()) {
            EmptyNotifications(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn {
                items(notifications, key = { it.id }) { notification ->
                    NotificationRow(notification)
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(notification: AppNotification) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val icon = if (notification.type == NotificationType.COMMENT) Icons.Filled.ChatBubble else Icons.Filled.ThumbUp
        NotificationIcon(icon)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(notification.text, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = relativeTimeText(notification.createdAtMillis),
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
            )
        }
    }
}

@Composable
private fun NotificationIcon(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = Modifier.size(36.dp).clip(CircleShape).background(VmpBlueContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = VmpBlue, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun EmptyNotifications(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Filled.Notifications,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(56.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text("No notifications yet", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Likes, comments, follows and friend requests will show up here.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted,
            textAlign = TextAlign.Center,
        )
    }
}
