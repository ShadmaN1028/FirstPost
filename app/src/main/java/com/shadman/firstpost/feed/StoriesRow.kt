package com.shadman.firstpost.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.Story
import com.shadman.firstpost.data.User
import com.shadman.firstpost.ui.theme.TextPrimary

// A row of coloured tiles standing in for story photos — same trick as the
// avatar placeholders, since there are no real photo assets for the
// synthetic cast. Decorative only, nothing here is tappable yet.
@Composable
fun StoriesRow(stories: List<Story>, authorOf: (String) -> User?, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        stories.forEach { story ->
            val author = authorOf(story.authorId)
            Column(
                modifier = Modifier
                    .width(96.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(story.backgroundColor)),
            ) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth())
                Text(
                    text = author?.name?.substringBefore(' ') ?: "Story",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}
