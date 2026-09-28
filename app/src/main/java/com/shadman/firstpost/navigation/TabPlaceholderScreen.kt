package com.shadman.firstpost.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlue
import com.shadman.firstpost.ui.theme.VmpBlueContainer

// Stand-in for tabs that aren't built. Big title like VMP's "Feed"/"Friends",
// plus a deliberate empty state so it never looks like a blank screen.
@Composable
fun TabPlaceholderScreen(tab: BottomTab, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = tab.label,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp, end = 16.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(VmpBlueContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(tab.icon, contentDescription = null, tint = VmpBlue, modifier = Modifier.size(34.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text(text = "${tab.label} is coming soon", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "This part of VMP isn't included in the demo yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center,
            )
        }
    }
}
