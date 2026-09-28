package com.shadman.firstpost.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.Comment
import com.shadman.firstpost.data.User
import com.shadman.firstpost.onboarding.AvatarPhoto
import com.shadman.firstpost.onboarding.initialsFrom
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.TextMuted

// Opened by tapping a post's Comment action. Read-only for this demo — no
// composer of its own, just what's already there (the welcome comment).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSheet(comments: List<Comment>, authorOf: (String) -> User?, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = NavySurface) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).navigationBarsPadding()) {
            Text("Comments", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            if (comments.isEmpty()) {
                Text(
                    text = "No comments yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 24.dp),
                )
            } else {
                comments.forEach { comment -> CommentRow(comment, authorOf(comment.authorId)) }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CommentRow(comment: Comment, author: User?) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        AvatarPhoto(photoUri = author?.photoUri, initials = initialsFrom(author?.name.orEmpty()), size = 36.dp)
        Spacer(Modifier.width(10.dp))
        Column {
            Text(author?.name ?: "Someone", style = MaterialTheme.typography.titleSmall)
            Text(comment.text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
