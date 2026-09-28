package com.shadman.firstpost.data

// The header line above a post's caption. Shared by the Say hello preview
// (built from draft fields, before a Post exists) and the feed's real
// PostCard (built from an actual Post), so the two never drift apart.
fun postHeaderText(authorName: String, isProfilePictureUpdate: Boolean, isFirstPost: Boolean): String =
    when {
        isProfilePictureUpdate -> "$authorName updated their profile picture"
        isFirstPost -> "$authorName created a new post"
        else -> authorName
    }

// "Just now" / "12m" / "3h" / "2d" — coarse on purpose, this is a demo feed.
fun relativeTimeText(createdAtMillis: Long, nowMillis: Long = System.currentTimeMillis()): String {
    val minutes = (nowMillis - createdAtMillis) / 60_000
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m"
        minutes < 60 * 24 -> "${minutes / 60}h"
        else -> "${minutes / (60 * 24)}d"
    }
}
