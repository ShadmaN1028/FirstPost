package com.shadman.firstpost.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.Constants
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlue

// The mock Android account picker: one invented account, tap to pick it.
// Dumb: renders the one account, reports the tap. No real Google account,
// no network — this is exactly what CLAUDE.md calls for.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleAccountPickerSheet(
    onAccountSelected: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = NavySurface,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Choose an account", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(onClick = onAccountSelected)
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(VmpBlue, CircleShape),
                ) {
                    Text(
                        text = Constants.MockGoogleAccount.NAME.first().toString(),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(Constants.MockGoogleAccount.NAME, style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = Constants.MockGoogleAccount.EMAIL,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted,
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
