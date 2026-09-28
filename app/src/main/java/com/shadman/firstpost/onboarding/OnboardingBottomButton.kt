package com.shadman.firstpost.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue

// The one full-width action button pinned at the bottom of every onboarding
// step. `filled` picks between the primary look (Next/Done) and the
// outlined look (Skip) — the same button just changes label and style, the
// way the real app's bio/gender/interests steps do.
@Composable
fun OnboardingBottomButton(
    text: String,
    filled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    if (filled) {
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VmpBlue,
                contentColor = Color.White,
                disabledContainerColor = VmpBlue.copy(alpha = 0.35f),
                disabledContentColor = Color.White.copy(alpha = 0.6f),
            ),
            modifier = modifier.fillMaxWidth().height(56.dp),
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, OutlineNavy),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
            modifier = modifier.fillMaxWidth().height(56.dp),
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}
