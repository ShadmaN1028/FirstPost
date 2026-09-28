package com.shadman.firstpost.friends

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.data.User
import com.shadman.firstpost.onboarding.AvatarPhoto
import com.shadman.firstpost.onboarding.initialsFrom
import com.shadman.firstpost.ui.theme.VmpBlue

// "Friends you may know" — Add Friend is local only, but it really does
// write to AppRepository.friends, the same list Build your circle fills.
@Composable
fun FriendsScreen(modifier: Modifier = Modifier) {
    val friends by AppRepository.friends.collectAsState()
    val addedIds = friends.map { it.id }.toSet()
    val suggestions = AppRepository.users.filterNot { it.id in addedIds }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Text(
                text = "Friends",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 4.dp),
            )
            Text(
                text = "Friends you may know",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }
        items(suggestions, key = { it.id }) { user ->
            FriendSuggestionRow(user = user, onAddClick = { AppRepository.addFriend(user) })
        }
        item { Spacer(Modifier.padding(bottom = 16.dp)) }
    }
}

@Composable
private fun FriendSuggestionRow(user: User, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AvatarPhoto(photoUri = user.photoUri, initials = initialsFrom(user.name), size = 48.dp)
        Spacer(Modifier.width(12.dp))
        Text(user.name, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VmpBlue, contentColor = Color.White),
        ) {
            Text("Add Friend")
        }
    }
}
