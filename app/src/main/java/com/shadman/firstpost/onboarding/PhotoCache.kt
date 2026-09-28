package com.shadman.firstpost.onboarding

import android.content.Context
import android.net.Uri
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Android's Photo Picker only guarantees the picker's own content:// Uri is
// readable for a limited time — not necessarily still open by the time the
// user reaches the feed or profile, sections later. So the moment a photo
// is picked, this copies it into the app's own cache folder and everything
// downstream reads that copy instead of the original picker Uri.
suspend fun copyPickedPhotoToCache(context: Context, sourceUri: Uri): String? =
    withContext(Dispatchers.IO) {
        runCatching {
            val destination = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                destination.outputStream().use { output -> input.copyTo(output) }
            }
            Uri.fromFile(destination).toString()
        }.getOrNull()
    }
