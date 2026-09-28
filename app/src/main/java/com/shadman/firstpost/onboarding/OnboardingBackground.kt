package com.shadman.firstpost.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shadman.firstpost.ui.theme.NavyBackground

// The app draws edge-to-edge (see MainActivity), so every screen is
// responsible for its own system-bar insets. This is the one place that
// handles it for onboarding: every screen from the welcome screen through
// step 08 wraps its content in this, so none of them draw under the status
// or navigation bar, and none of them repeat the insets logic themselves.
@Composable
fun OnboardingBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NavyBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = content,
    )
}
