package com.shadman.firstpost.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.data.PremiumStatus
import com.shadman.firstpost.feed.PostCard
import com.shadman.firstpost.onboarding.AvatarPhoto
import com.shadman.firstpost.onboarding.initialsFrom
import com.shadman.firstpost.ui.theme.NavyBackground
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlueContainer

// The current user's own profile, opened by tapping their avatar. Shows
// what onboarding collected, the Premium badge when a trial is active, and
// every post they've made — including the one from Say hello.
@Composable
fun ProfileScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    val currentUser by AppRepository.currentUser.collectAsState()
    val allPosts by AppRepository.posts.collectAsState()
    val ownPosts = allPosts.filter { it.authorId == currentUser?.id }
    val premiumStatus = AppRepository.premiumStatus(currentUser)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyBackground)
            .navigationBarsPadding(),
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                AvatarPhoto(
                    photoUri = currentUser?.photoUri,
                    initials = initialsFrom(currentUser?.name.orEmpty()),
                    size = 88.dp,
                )
                Spacer(Modifier.height(12.dp))
                Text(currentUser?.name.orEmpty(), style = MaterialTheme.typography.headlineLarge)
                if (!currentUser?.username.isNullOrBlank()) {
                    Text("@${currentUser?.username}", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                }
                if (!currentUser?.bio.isNullOrBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(currentUser?.bio.orEmpty(), style = MaterialTheme.typography.bodyLarge)
                }
                if (premiumStatus is PremiumStatus.Trial) {
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 50))
                            .background(VmpBlueContainer)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    ) {
                        Text(
                            text = "💎 Trial · ${premiumStatus.daysLeft} day${if (premiumStatus.daysLeft == 1) "" else "s"} left",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Posts", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
            }
        }
        if (ownPosts.isEmpty()) {
            item {
                Text(
                    text = "You haven't posted anything yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
        items(ownPosts, key = { it.id }) { post ->
            PostCard(
                post = post,
                author = currentUser,
                isLiked = AppRepository.isLikedByCurrentUser(post),
                onLikeClick = { AppRepository.toggleLike(post.id) },
                authorOf = AppRepository::userById,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            )
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}
