package com.shadman.firstpost.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.shadman.firstpost.ui.theme.VmpBlue
import kotlinx.coroutines.launch

// "01/08 Add a profile photo". No back arrow — there's nothing before this
// in onboarding. Uses Android's built-in Photo Picker, which needs no
// runtime permission, so there's no permission-request UI to build.
@Composable
fun Step01Screen(
    onboardingViewModel: OnboardingViewModel,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val draft by onboardingViewModel.draft.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // The picker's own Uri isn't guaranteed to stay readable by the time the
    // feed or profile need it, so the photo is copied into the app's cache
    // folder right away and that copy is what gets saved to the draft.
    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                onboardingViewModel.setPhoto(copyPickedPhotoToCache(context, uri))
            }
        }
    }
    val launchPicker = {
        pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    OnboardingStepScaffold(
        step = 1,
        title = "Add a profile photo",
        subtitle = "A great photo makes your circle feel real.",
        onBackClick = null,
        modifier = modifier,
        content = {
            Column(
                modifier = Modifier.fillMaxSize().fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AvatarPhoto(
                    photoUri = draft.photoUri,
                    initials = initialsFrom(draft.name),
                    size = 140.dp,
                )
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = launchPicker) {
                    Text(
                        text = if (draft.photoUri == null) "Choose from Gallery" else "Change photo",
                        color = VmpBlue,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        },
        bottomBar = {
            if (draft.photoUri == null) {
                OnboardingBottomButton(text = "Choose from Gallery", filled = true, onClick = launchPicker)
                Spacer(Modifier.height(12.dp))
                OnboardingBottomButton(text = "Skip for now", filled = false, onClick = onNextClick)
            } else {
                OnboardingBottomButton(text = "Next", filled = true, onClick = onNextClick)
            }
        },
    )
}
