package com.shadman.firstpost.navigation

// Top-level destinations. The app is either onboarding or inside the main app.
object Routes {
    // Route of the nested onboarding graph itself — used to scope the one
    // shared OnboardingViewModel to every screen inside it.
    const val ONBOARDING_GRAPH = "onboarding"

    const val WELCOME = "onboarding/welcome"
    const val STEP_01 = "onboarding/step01"
    const val STEP_02 = "onboarding/step02"
    const val STEP_03 = "onboarding/step03"
    const val STEP_04 = "onboarding/step04"
    const val STEP_05 = "onboarding/step05"
    const val STEP_06 = "onboarding/step06"
    const val STEP_07 = "onboarding/step07"
    const val STEP_08 = "onboarding/step08"
    const val CIRCLE = "onboarding/circle"
    const val NOTIFICATION_PERMISSION = "onboarding/notification-permission"

    const val MAIN = "main"

    // Full-screen destinations reached from inside the main app, but kept
    // outside it so they render without the bottom nav bar.
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val COMPOSER = "composer"
    const val PAYWALL = "paywall"
}
