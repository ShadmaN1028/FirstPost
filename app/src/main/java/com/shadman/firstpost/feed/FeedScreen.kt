package com.shadman.firstpost.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.ui.theme.NavyBackground

// The Home tab. Composer bar, stories, "New members" (first posts get a
// boost — see CLAUDE.md's earned-trial design), then the regular timeline.
// The earned-Premium dialog itself is shown right where a post is made
// (Say hello, the composer) — not here, so it never shows twice.
@Composable
fun FeedScreen(
    onOpenProfile: () -> Unit,
    onOpenComposer: () -> Unit,
    onOpenNotifications: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentUser by AppRepository.currentUser.collectAsState()
    val allPosts by AppRepository.posts.collectAsState()
    val notifications by AppRepository.notifications.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    val (firstPosts, timelinePosts) = allPosts.partition { it.isFirstPost }
    val orderedFirstPosts = firstPosts.sortedByDescending { it.authorId == currentUser?.id }

    LazyColumn(modifier = modifier.fillMaxSize().background(NavyBackground)) {
        item {
            FeedTopBar(unreadNotificationCount = unreadCount, onNotificationsClick = onOpenNotifications)
            ComposerBar(
                currentUser = currentUser,
                onAvatarClick = onOpenProfile,
                onComposerClick = onOpenComposer,
            )
            StoriesRow(stories = AppRepository.stories, authorOf = AppRepository::userById)
            Spacer(Modifier.height(12.dp))
            NewMembersSection(
                posts = orderedFirstPosts,
                authorOf = AppRepository::userById,
                isLiked = AppRepository::isLikedByCurrentUser,
                onLikeClick = { post -> AppRepository.toggleLike(post.id) },
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "More posts",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(8.dp))
        }
        items(timelinePosts, key = { it.id }) { post ->
            PostCard(
                post = post,
                author = AppRepository.userById(post.authorId),
                isLiked = AppRepository.isLikedByCurrentUser(post),
                onLikeClick = { AppRepository.toggleLike(post.id) },
                authorOf = AppRepository::userById,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            )
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}
