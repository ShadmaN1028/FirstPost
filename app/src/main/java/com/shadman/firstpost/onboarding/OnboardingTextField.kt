package com.shadman.firstpost.onboarding

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue

// The underlined, background-free text field used on the name, username and
// bio steps (see the VMP screenshots — no box, just a bottom rule). One
// place to keep that look consistent instead of repeating the colour
// overrides on every step.
@Composable
fun OnboardingTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    minLines: Int = 1,
    prefix: (@Composable () -> Unit)? = null,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextMuted, style = MaterialTheme.typography.titleMedium) },
        prefix = prefix,
        singleLine = singleLine,
        minLines = minLines,
        textStyle = MaterialTheme.typography.titleMedium.copy(color = TextPrimary),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = VmpBlue,
            unfocusedIndicatorColor = OutlineNavy,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = VmpBlue,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}
