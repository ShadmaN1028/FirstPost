package com.shadman.firstpost.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.Constants
import com.shadman.firstpost.ui.theme.TextMuted

// The layout every onboarding step 01-08 shares: back arrow (or a same-sized
// blank spacer on step 01, so the header never jumps), step counter, title,
// subtitle, then the step's own content, then its own bottom button(s).
// Built once here so adding a step is just supplying title/subtitle/content
// instead of re-laying out the screen.
@Composable
fun OnboardingStepScaffold(
    step: Int,
    title: String,
    subtitle: String,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
    bottomBar: @Composable ColumnScope.() -> Unit,
) {
    OnboardingBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            if (onBackClick != null) {
                OnboardingBackButton(onClick = onBackClick)
            } else {
                Spacer(Modifier.height(48.dp))
            }
            Spacer(Modifier.height(8.dp))
            StepHeader(step = step, totalSteps = Constants.ONBOARDING_STEP_COUNT)
            Spacer(Modifier.height(20.dp))
            Text(title, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(6.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                content = content,
            )
            bottomBar()
            Spacer(Modifier.height(12.dp))
        }
    }
}
