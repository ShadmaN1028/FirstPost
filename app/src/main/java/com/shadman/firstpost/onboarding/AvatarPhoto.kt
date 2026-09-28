package com.shadman.firstpost.onboarding

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.shadman.firstpost.ui.theme.VmpBlue
import com.shadman.firstpost.ui.theme.VmpBlueContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// The round avatar used on step 01 (photo picker) and step 08 (Say hello
// preview): the picked photo if there is one, otherwise the user's initials
// on a blue circle. No image-loading library needed — the picked photo is a
// content:// Uri decoded once with the platform's own ImageDecoder.
@Composable
fun AvatarPhoto(
    photoUri: String?,
    initials: String,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val bitmap = rememberUriImageBitmap(photoUri)
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(VmpBlueContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "Profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Text(
                text = initials.ifBlank { "?" },
                style = MaterialTheme.typography.headlineLarge,
                color = VmpBlue,
            )
        }
    }
}

@Composable
private fun rememberUriImageBitmap(uri: String?): ImageBitmap? {
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    LaunchedEffect(uri) {
        bitmap = uri?.let { loadImageBitmap(context, it) }
    }
    return bitmap
}

private suspend fun loadImageBitmap(context: Context, uriString: String): ImageBitmap? =
    withContext(Dispatchers.IO) {
        runCatching {
            val source = ImageDecoder.createSource(context.contentResolver, Uri.parse(uriString))
            ImageDecoder.decodeBitmap(source).asImageBitmap()
        }.getOrNull()
    }
