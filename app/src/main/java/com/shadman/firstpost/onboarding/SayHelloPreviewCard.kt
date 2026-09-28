package com.shadman.firstpost.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.postHeaderText
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue
import com.shadman.firstpost.ui.theme.VmpBlueContainer

// Renders exactly what the feed post will look like, so Say hello is a
// preview, not a guess. With a photo it's a "<Name> updated their profile
// picture" post — the new photo shown large in a circle on a card backing,
// the way Facebook shows a profile-picture update. Without a photo it's a
// plain text post: just the name and caption. The caption is edited right
// here — there's no separate field anywhere else on the screen.
@Composable
fun SayHelloPreviewCard(
    authorName: String,
    authorInitials: String,
    photoUri: String?,
    caption: String,
    onCaptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NavySurface)
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarPhoto(photoUri = photoUri, initials = authorInitials, size = 44.dp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = postHeaderText(authorName, isProfilePictureUpdate = photoUri != null, isFirstPost = true),
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Just now", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Filled.Public,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(12.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Public", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        EditableCaption(caption = caption, onCaptionChange = onCaptionChange)

        if (photoUri != null) {
            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VmpBlueContainer),
                contentAlignment = Alignment.Center,
            ) {
                AvatarPhoto(photoUri = photoUri, initials = "", size = 160.dp)
            }
        }
    }
}

// A caption that looks like plain post text but is actually editable —
// no border, no background, just a cursor. A manual placeholder overlay
// stands in since BasicTextField has no built-in one.
@Composable
private fun EditableCaption(caption: String, onCaptionChange: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = caption,
            onValueChange = onCaptionChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextPrimary),
            cursorBrush = SolidColor(VmpBlue),
            modifier = Modifier.fillMaxWidth(),
        )
        if (caption.isEmpty()) {
            Text(
                text = "Write a caption...",
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted,
            )
        }
    }
}
