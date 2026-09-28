package com.shadman.firstpost.onboarding

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue

// Shown when "Skip for now" is tapped on Say hello — one more neutral offer
// of the trial before actually skipping, not a guilt trip.
@Composable
fun SkipFirstPostDialog(
    onPostInstead: () -> Unit,
    onConfirmSkip: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NavySurface,
        titleContentColor = TextPrimary,
        textContentColor = TextPrimary,
        title = { Text("Skip your first post?") },
        text = {
            Text(
                "Posting now unlocks 2 days of Premium: your posts are shown first. " +
                    "You can post later, but the free Premium is only for your first post today.",
            )
        },
        confirmButton = {
            TextButton(onClick = onPostInstead) { Text("Post & get Premium", color = VmpBlue) }
        },
        dismissButton = {
            TextButton(onClick = onConfirmSkip) { Text("Skip", color = TextPrimary) }
        },
    )
}
