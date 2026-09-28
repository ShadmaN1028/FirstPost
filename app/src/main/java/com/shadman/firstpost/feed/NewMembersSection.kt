package com.shadman.firstpost.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.Post
import com.shadman.firstpost.data.User

// Every "first post" in the feed — the current user's own, if they made
// one, plus the invented new members' — shown together so a first post
// never lands with zero visibility. FeedScreen sorts the current user's
// post to the front before passing it in. See CLAUDE.md's earned-trial
// design for why a first post gets this boost.
@Composable
fun NewMembersSection(
    posts: List<Post>,
    authorOf: (String) -> User?,
    isLiked: (Post) -> Boolean,
    onLikeClick: (Post) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (posts.isEmpty()) return

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text("New members", style = MaterialTheme.typography.titleMedium)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 12.dp)) {
            posts.forEach { post ->
                PostCard(
                    post = post,
                    author = authorOf(post.authorId),
                    isLiked = isLiked(post),
                    onLikeClick = { onLikeClick(post) },
                    authorOf = authorOf,
                )
            }
        }
    }
}
