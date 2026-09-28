package com.shadman.firstpost.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shadman.firstpost.Constants

// Wires WelcomeScreen and the mock account picker to WelcomeViewModel, and
// moves on once sign-in finishes. This is the one place that owns the
// WelcomeViewModel instance; the screens themselves stay dumb.
@Composable
fun WelcomeRoute(
    onboardingViewModel: OnboardingViewModel,
    onSignedIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val welcomeViewModel: WelcomeViewModel = viewModel()
    val uiState by welcomeViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSignedIn) {
        if (uiState.isSignedIn) {
            onboardingViewModel.prefillName(Constants.MockGoogleAccount.NAME)
            onSignedIn()
        }
    }

    WelcomeScreen(
        onSignInWithGoogleClick = welcomeViewModel::onSignInWithGoogleClick,
        modifier = modifier,
    )

    if (uiState.isAccountPickerVisible) {
        GoogleAccountPickerSheet(
            onAccountSelected = welcomeViewModel::onAccountSelected,
            onDismiss = welcomeViewModel::onDismissAccountPicker,
        )
    }
}
