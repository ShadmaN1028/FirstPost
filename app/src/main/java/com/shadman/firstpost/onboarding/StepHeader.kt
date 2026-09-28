package com.shadman.firstpost.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.VmpBlue

// The "01 / 08" counter + progress bar shown at the top of every onboarding
// step (see the VMP screenshots). Built now, reused by every step screen
// from Section 3 onward, so it's written once.
@Composable
fun StepHeader(step: Int, totalSteps: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "%02d / %02d".format(step, totalSteps),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { step / totalSteps.toFloat() },
            color = VmpBlue,
            trackColor = OutlineNavy,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
        )
    }
}
