package com.shadman.firstpost.data

// Plain immutable data. No Android or Compose types, so every screen can share them.

data class User(
    val id: String,
    val name: String,
    val username: String,
    val avatarColor: Long,          // ARGB, background of the coloured-initial avatar
    val email: String? = null,      // only the current user has one, from mock sign-in
    val photoUri: String? = null,   // real photo, only the current user picks one
    val bio: String? = null,
    val gender: String? = null,
    val dateOfBirthMillis: Long? = null,
    val interests: List<String> = emptyList(),
    val signedUpAtMillis: Long? = null,  // only the current user has one; starts the 24h earned-trial window
    val trialEndMillis: Long? = null,    // null = never earned a trial; see PremiumStatus
) {
    // "Maya Chen" -> "MC", "Theo" -> "T".
    val initials: String
        get() = name.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
}

enum class ReactionType { LIKE, LOVE, HAHA, WOW }

data class Reaction(
    val userId: String,
    val type: ReactionType,
)

data class Comment(
    val id: String,
    val authorId: String,
    val text: String,
    val createdAtMillis: Long,
)

data class Post(
    val id: String,
    val authorId: String,
    val text: String,
    val imageUri: String? = null,
    val createdAtMillis: Long,
    val reactions: List<Reaction> = emptyList(),
    val comments: List<Comment> = emptyList(),
    // true = renders with the "<Name> updated their profile picture" header.
    val isProfilePictureUpdate: Boolean = false,
    // true only for a user's very first post — Section 7 schedules reactions
    // off it, and it's what the New members section shows.
    val isFirstPost: Boolean = false,
    val viewCount: Int = 0,
) {
    val likeCount: Int get() = reactions.size
    val commentCount: Int get() = comments.size
}

// A user's Premium standing, derived from User.trialEndMillis + the clock —
// never stored as its own flag, so it can't drift out of sync.
sealed class PremiumStatus {
    data object None : PremiumStatus()
    data class Trial(val daysLeft: Int) : PremiumStatus()
    data object Expired : PremiumStatus()
}

data class Story(
    val id: String,
    val authorId: String,
    val backgroundColor: Long,  // ARGB, a solid colour tile stands in for a photo
    val caption: String,
)

enum class NotificationType { REACTION, COMMENT, FRIEND_REQUEST }

// Named AppNotification so it never clashes with android.app.Notification.
data class AppNotification(
    val id: String,
    val type: NotificationType,
    val text: String,
    val createdAtMillis: Long,
    val postId: String? = null,
    val isRead: Boolean = false,
)
