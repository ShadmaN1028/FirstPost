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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUpOffAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.Post
import com.shadman.firstpost.data.User
import com.shadman.firstpost.data.postHeaderText
import com.shadman.firstpost.data.relativeTimeText
import com.shadman.firstpost.onboarding.AvatarPhoto
import com.shadman.firstpost.onboarding.initialsFrom
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlue
import com.shadman.firstpost.ui.theme.VmpBlueContainer

// One feed post, everywhere a post is shown (main feed, New members,
// profile). Dumb: renders a Post + its author, reports the one tap (Like)
// that has real behaviour in this demo.
@Composable
fun PostCard(
    post: Post,
    author: User?,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    authorOf: (String) -> User?,
    modifier: Modifier = Modifier,
) {
    val authorName = author?.name ?: "Someone"
    var showComments by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NavySurface)
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarPhoto(photoUri = author?.photoUri, initials = initialsFrom(authorName), size = 44.dp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = postHeaderText(authorName, post.isProfilePictureUpdate, post.isFirstPost),
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(relativeTimeText(post.createdAtMillis), style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Filled.Public, contentDescription = null, tint = TextMuted, modifier = Modifier.height(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Public", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                }
            }
        }

        if (post.text.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(post.text, style = MaterialTheme.typography.bodyLarge)
        }

        if (post.isProfilePictureUpdate && post.imageUri != null) {
            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VmpBlueContainer),
                contentAlignment = Alignment.Center,
            ) {
                AvatarPhoto(photoUri = post.imageUri, initials = "", size = 160.dp)
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("${post.likeCount} likes · ${post.commentCount} comments", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Visibility, contentDescription = null, tint = TextMuted, modifier = Modifier.height(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("${post.viewCount}", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            }
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = OutlineNavy, thickness = 1.dp)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            PostAction(
                icon = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUpOffAlt,
                label = "Like",
                tint = if (isLiked) VmpBlue else TextMuted,
                onClick = onLikeClick,
            )
            PostAction(
                icon = Icons.Filled.ChatBubbleOutline,
                label = "Comment",
                tint = TextMuted,
                onClick = { showComments = true },
            )
            PostAction(icon = Icons.Filled.Share, label = "Share", tint = TextMuted, onClick = {})
        }
    }

    if (showComments) {
        CommentsSheet(comments = post.comments, authorOf = authorOf, onDismiss = { showComments = false })
    }
}

@Composable
private fun PostAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick).padding(vertical = 4.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.height(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, color = tint)
    }
}
