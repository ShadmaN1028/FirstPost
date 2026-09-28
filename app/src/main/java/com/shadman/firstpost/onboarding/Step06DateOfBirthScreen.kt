package com.shadman.firstpost.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.NavySurface
import com.shadman.firstpost.ui.theme.OutlineNavy
import com.shadman.firstpost.ui.theme.TextMuted
import com.shadman.firstpost.ui.theme.TextPrimary
import com.shadman.firstpost.ui.theme.VmpBlue

// "06/08 Your date of birth". Not skippable — there's a value or there
// isn't, so Next stays disabled until a date is picked.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step06DateOfBirthScreen(
    onboardingViewModel: OnboardingViewModel,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val draft by onboardingViewModel.draft.collectAsState()
    var showPicker by remember { mutableStateOf(false) }

    OnboardingStepScaffold(
        step = 6,
        title = "Your date of birth",
        subtitle = "Add your birthday so people can see your age. You control who sees it.",
        onBackClick = onBackClick,
        modifier = modifier,
        content = {
            val dateOfBirthMillis = draft.dateOfBirthMillis
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NavySurface)
                    .border(1.dp, if (dateOfBirthMillis != null) VmpBlue else OutlineNavy, RoundedCornerShape(14.dp))
                    .clickable { showPicker = true }
                    .padding(16.dp),
            ) {
                Text(
                    text = dateOfBirthMillis?.let(::formatDateOfBirth) ?: "Select your date of birth",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (dateOfBirthMillis != null) TextPrimary else TextMuted,
                )
                if (dateOfBirthMillis != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${ageFromDateOfBirth(dateOfBirthMillis)} years old",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted,
                    )
                }
            }
        },
        bottomBar = {
            OnboardingBottomButton(text = "Next", filled = true, enabled = draft.isDateOfBirthValid, onClick = onNextClick)
        },
    )

    if (showPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = draft.dateOfBirthMillis ?: defaultDateOfBirthMillis(),
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let(onboardingViewModel::setDateOfBirth)
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = pickerState)
        }
    }
}
