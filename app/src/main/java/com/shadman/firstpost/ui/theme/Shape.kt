package com.shadman.firstpost.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Corner radii measured off the VMP screenshots.
val VmpShapes = Shapes(
    small = RoundedCornerShape(10.dp),   // buttons
    medium = RoundedCornerShape(16.dp),  // cards, story tiles
    large = RoundedCornerShape(24.dp),   // bottom nav top corners, sheets
)

// Fully rounded ends: filter chips, the "Create a post" field.
val PillShape = RoundedCornerShape(percent = 50)
