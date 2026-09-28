package com.shadman.firstpost.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PhoneInTalk
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.ui.graphics.vector.ImageVector

// The six bottom-nav tabs, in VMP's order. Each tab's route is also its screen.
enum class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME("tab/home", "Feed", Icons.Outlined.Home),
    CHATS("tab/chats", "Chats", Icons.AutoMirrored.Outlined.Chat),
    CALLS("tab/calls", "Calls", Icons.Outlined.PhoneInTalk),
    FRIENDS("tab/friends", "Friends", Icons.Outlined.People),
    MARKETPLACE("tab/marketplace", "Marketplace", Icons.Outlined.Storefront),
    SETTINGS("tab/settings", "Settings", Icons.Outlined.Settings);

    companion object {
        fun fromRoute(route: String?): BottomTab? = entries.find { it.route == route }
    }
}
