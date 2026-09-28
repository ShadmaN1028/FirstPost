package com.shadman.firstpost.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.onboarding.OnboardingBottomButton
import com.shadman.firstpost.ui.theme.NavyBackground
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.VmpBlue

private data class Plan(val name: String, val price: String, val highlighted: Boolean = false)

private val PLANS = listOf(
    Plan("Messenger Elite", "BDT 70,000.00 per month"),
    Plan("Messenger Platinum", "3 days free, then BDT 7,700.00 per month", highlighted = true),
    Plan("Messenger Gold", "3 days free, then BDT 6,300.00 per month"),
)

// Shown once a trial has ended — by time, or by the Settings demo button.
// No billing: every tap just says so.
@Composable
fun PaywallScreen(onCloseClick: () -> Unit, modifier: Modifier = Modifier) {
    var demoMessageVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Restore", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
            Text("Choose a plan", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text("Your Premium trial has ended", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                PLANS.forEach { plan ->
                    PlanCard(plan = plan, onClick = { demoMessageVisible = true })
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Stop getting scrolled past.", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            FeatureRow(Icons.Filled.TrendingUp, "Get seen first", "You appear above free members in Discover and People You May Know")
            Spacer(Modifier.height(16.dp))
            FeatureRow(Icons.Filled.WorkspacePremium, "Be taken seriously", "Your badge shows people you are a real member — on your profile, in search and in every member list")
            Spacer(Modifier.height(16.dp))
            FeatureRow(Icons.Filled.Dialpad, "Pick your own number", "Choose the digits people reach you on — a date, a run, a number they remember")
        }

        Spacer(Modifier.weight(1f))

        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            if (demoMessageVisible) {
                Text(
                    text = "Demo only",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                )
            }
            OnboardingBottomButton(
                text = "Start 3-day free trial",
                filled = true,
                onClick = { demoMessageVisible = true },
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "A subscription is not required to use VMP. Auto-renews until cancelled.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun PlanCard(plan: Plan, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(NavySurface)
            .clickable(onClick = onClick)
            .padding(14.dp),
    ) {
        Text(plan.name, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))
        Text(plan.price, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
    }
}

@Composable
private fun FeatureRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
    Row {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(OutlineNavy),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = VmpBlue, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        }
    }
}
