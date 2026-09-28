package com.shadman.firstpost.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue

// Shown immediately after a post earns the trial — from Say hello or the
// feed composer, whichever one actually triggered it. Shared by both so the
// wording can't drift between the two places it can appear.
@Composable
fun EarnedPremiumDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NavySurface,
        titleContentColor = TextPrimary,
        textContentColor = TextPrimary,
        title = { Text("🎉 You've earned 2 days of Premium.") },
        text = { Text("Your posts get seen first.") },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Nice!", color = VmpBlue) }
        },
    )
}
