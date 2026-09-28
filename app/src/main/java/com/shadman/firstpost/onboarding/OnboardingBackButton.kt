package com.shadman.firstpost.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// The back arrow shown on steps 02-07 (see the VMP screenshots). Step 01
// has no way back — there's nothing before it in onboarding — so it omits
// this entirely rather than showing a disabled one.
@Composable
fun OnboardingBackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }
}
