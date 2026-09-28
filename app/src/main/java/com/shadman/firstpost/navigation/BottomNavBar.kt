package com.shadman.firstpost.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.IconInactive
import com.shadman.firstpost.ui.theme.NavyBackground
import com.shadman.firstpost.ui.theme.VmpBlue
import com.shadman.firstpost.ui.theme.VmpBlueContainer

// VMP's icon-only bottom bar: rounded top corners, the selected tab sits in a
// dark-blue pill with a blue icon. Dumb composable: shows state, reports taps.
@Composable
fun BottomNavBar(
    selectedTab: BottomTab?,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large.copy(bottomStart = CornerZero, bottomEnd = CornerZero))
            .background(NavyBackground)
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BottomTab.entries.forEach { tab ->
            TabItem(
                tab = tab,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun TabItem(
    tab: BottomTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(18.dp))
                .background(if (isSelected) VmpBlueContainer else Color.Transparent)
                .selectable(selected = isSelected, onClick = onClick, role = Role.Tab),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = if (isSelected) VmpBlue else IconInactive,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

private val CornerZero = CornerSize(0.dp)
