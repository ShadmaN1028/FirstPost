package com.shadman.firstpost.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlue

// Dumb: renders the welcome screen, reports one event. No sign-in logic here.
@Composable
fun WelcomeScreen(
    onSignInWithGoogleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        ) {
            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(VmpBlue, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text("V", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = "VMP Messenger",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Chat, call and share with the people who matter.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onSignInWithGoogleClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1F1F1F),
                ),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                GoogleMonogram()
                Spacer(Modifier.width(12.dp))
                Text("Sign in with Google", fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}

// A generic "G" badge standing in for the real Google logo — this is a mock
// sign-in with no real Google account involved, so it's deliberately not the
// actual Google artwork.
@Composable
private fun GoogleMonogram() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(Color(0xFF4285F4), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text("G", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
