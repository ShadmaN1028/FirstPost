package com.shadman.firstpost.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.feed.FeedScreen
import com.shadman.firstpost.friends.FriendsScreen
import com.shadman.firstpost.settings.SettingsScreen

// The main app: bottom nav + whichever tab is selected.
// Has its own NavController, separate from the onboarding/main one.
@Composable
fun MainScaffold(
    onOpenProfile: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenComposer: () -> Unit,
    onOpenPaywall: () -> Unit,
) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val selectedTab = BottomTab.fromRoute(backStackEntry?.destination?.route)

    // The reaction system notification carries a flag asking to open
    // Notifications; MainActivity sets it, this watches for it wherever the
    // user currently is in the app, and clears it once acted on.
    val openNotificationsRequested by AppRepository.openNotificationsRequested.collectAsState()
    LaunchedEffect(openNotificationsRequested) {
        if (openNotificationsRequested) {
            onOpenNotifications()
            AppRepository.consumeOpenNotificationsRequest()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { tab -> tabNavController.switchTo(tab) },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = BottomTab.HOME.route,
            modifier = Modifier.padding(innerPadding),
            // Tabs swap instantly, like the real app, instead of cross-fading.
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            composable(BottomTab.HOME.route) {
                FeedScreen(
                    onOpenProfile = onOpenProfile,
                    onOpenComposer = onOpenComposer,
                    onOpenNotifications = onOpenNotifications,
                )
            }
            composable(BottomTab.FRIENDS.route) { FriendsScreen() }
            composable(BottomTab.SETTINGS.route) { SettingsScreen(onOpenPaywall = onOpenPaywall) }
            // Chats, Calls and Marketplace stay placeholders — out of scope
            // per CLAUDE.md.
            listOf(BottomTab.CHATS, BottomTab.CALLS, BottomTab.MARKETPLACE).forEach { tab ->
                composable(tab.route) { TabPlaceholderScreen(tab) }
            }
        }
    }
}

// Standard bottom-nav behaviour: one copy of each tab on the back stack,
// each tab keeps its scroll position, Back from any tab returns to Home.
private fun NavHostController.switchTo(tab: BottomTab) {
    navigate(tab.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
