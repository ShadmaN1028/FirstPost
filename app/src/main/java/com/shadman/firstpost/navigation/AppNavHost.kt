package com.shadman.firstpost.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.shadman.firstpost.circle.CircleScreen
import com.shadman.firstpost.feed.ComposerScreen
import com.shadman.firstpost.notifications.NotificationPermissionScreen
import com.shadman.firstpost.notifications.NotificationsScreen
import com.shadman.firstpost.onboarding.OnboardingViewModel
import com.shadman.firstpost.onboarding.Step01Screen
import com.shadman.firstpost.onboarding.Step02NameScreen
import com.shadman.firstpost.onboarding.Step03UsernameScreen
import com.shadman.firstpost.onboarding.Step04BioScreen
import com.shadman.firstpost.onboarding.Step05GenderScreen
import com.shadman.firstpost.onboarding.Step06DateOfBirthScreen
import com.shadman.firstpost.onboarding.Step07InterestedInScreen
import com.shadman.firstpost.onboarding.Step08SayHelloScreen
import com.shadman.firstpost.onboarding.WelcomeRoute
import com.shadman.firstpost.paywall.PaywallScreen
import com.shadman.firstpost.profile.ProfileScreen

// Top-level navigation: onboarding first, then the main app. Profile,
// Notifications, the composer and the paywall are reached from inside the
// main app but live here too, so they render full-screen without the
// bottom nav bar MainScaffold otherwise always shows.
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.ONBOARDING_GRAPH) {
        navigation(startDestination = Routes.WELCOME, route = Routes.ONBOARDING_GRAPH) {
            composable(Routes.WELCOME) {
                WelcomeRoute(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    onSignedIn = { navController.navigateForward(Routes.WELCOME, Routes.STEP_01) },
                )
            }
            composable(Routes.STEP_01) {
                Step01Screen(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    onNextClick = { navController.navigate(Routes.STEP_02) },
                )
            }
            composable(Routes.STEP_02) {
                Step02NameScreen(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    onBackClick = { navController.popBackStack() },
                    onNextClick = { navController.navigate(Routes.STEP_03) },
                )
            }
            composable(Routes.STEP_03) {
                Step03UsernameScreen(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    onBackClick = { navController.popBackStack() },
                    onNextClick = { navController.navigate(Routes.STEP_04) },
                )
            }
            composable(Routes.STEP_04) {
                Step04BioScreen(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    onBackClick = { navController.popBackStack() },
                    onContinueClick = { navController.navigate(Routes.STEP_05) },
                )
            }
            composable(Routes.STEP_05) {
                Step05GenderScreen(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    onBackClick = { navController.popBackStack() },
                    onContinueClick = { navController.navigate(Routes.STEP_06) },
                )
            }
            composable(Routes.STEP_06) {
                Step06DateOfBirthScreen(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    onBackClick = { navController.popBackStack() },
                    onNextClick = { navController.navigate(Routes.STEP_07) },
                )
            }
            composable(Routes.STEP_07) {
                Step07InterestedInScreen(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    onBackClick = { navController.popBackStack() },
                    onDoneClick = { navController.navigate(Routes.STEP_08) },
                )
            }
            composable(Routes.STEP_08) {
                Step08SayHelloScreen(
                    onboardingViewModel = navController.sharedOnboardingViewModel(),
                    // Post and Skip both move on; popUpTo drops step08 so
                    // Back can't return here and fire a second "first" post.
                    onPostClick = { navController.navigateForward(Routes.STEP_08, Routes.CIRCLE) },
                    onSkipClick = { navController.navigateForward(Routes.STEP_08, Routes.CIRCLE) },
                )
            }
            composable(Routes.CIRCLE) {
                CircleScreen(
                    onContinueClick = { navController.navigateForward(Routes.CIRCLE, Routes.NOTIFICATION_PERMISSION) },
                )
            }
            composable(Routes.NOTIFICATION_PERMISSION) {
                NotificationPermissionScreen(
                    onContinueClick = { navController.navigateForward(Routes.ONBOARDING_GRAPH, Routes.MAIN) },
                )
            }
        }
        composable(Routes.MAIN) {
            MainScaffold(
                onOpenProfile = { navController.navigate(Routes.PROFILE) },
                onOpenNotifications = { navController.navigate(Routes.NOTIFICATIONS) },
                onOpenComposer = { navController.navigate(Routes.COMPOSER) },
                onOpenPaywall = { navController.navigate(Routes.PAYWALL) },
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.COMPOSER) {
            ComposerScreen(
                onBackClick = { navController.popBackStack() },
                onPosted = { navController.popBackStack() },
            )
        }
        composable(Routes.PAYWALL) {
            PaywallScreen(onCloseClick = { navController.popBackStack() })
        }
    }
}

// One OnboardingViewModel per run through onboarding: scoped to the graph's
// own back-stack entry rather than each screen's, so every step 01-08
// screen gets the same instance and none of them lose state on Back/Next.
@Composable
private fun NavHostController.sharedOnboardingViewModel(): OnboardingViewModel =
    viewModel(getBackStackEntry(Routes.ONBOARDING_GRAPH))

// Sign-in (and any other one-way step) shouldn't leave the screen behind it
// on the back stack, or Back would return to a screen that already finished.
private fun NavHostController.navigateForward(from: String, to: String) {
    navigate(to) { popUpTo(from) { inclusive = true } }
}
