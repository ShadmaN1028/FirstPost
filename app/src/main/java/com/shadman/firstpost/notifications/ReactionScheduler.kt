package com.shadman.firstpost.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.shadman.firstpost.Constants
import com.shadman.firstpost.MainActivity
import com.shadman.firstpost.R
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.data.ReactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Runs the synthetic "boost" a first post gets: a few invented reactions
// arriving on a delay, plus the real system notification once three have
// landed. A plain Kotlin object with its own coroutine scope, not tied to
// any composable's lifecycle — it keeps running if the user switches
// screens or leaves the app entirely, same as a real backend push would.
object ReactionScheduler {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var appContext: Context? = null

    private const val CHANNEL_ID = "post_reactions"
    private const val NOTIFICATION_ID = 1001

    // Invented reactors — same cast as everywhere else in the app.
    private const val FIRST_LIKER = "u1"   // Maya Chen
    private const val COMMENTER = "u3"     // Lena Fischer
    private const val SECOND_LIKER = "u5"  // Sofia Alvarez
    private const val THIRD_LIKER = "u6"   // Kwame Owusu

    // Needs a real Context to create the notification channel and post to
    // it later. Called once from MainActivity.onCreate.
    fun init(context: Context) {
        appContext = context.applicationContext
        createNotificationChannel()
    }

    fun scheduleFor(postId: String, firstName: String) {
        scope.launch {
            delay(Constants.Reactions.FIRST_LIKE_DELAY_MILLIS)
            AppRepository.addReaction(postId, FIRST_LIKER, ReactionType.LIKE)

            delay(Constants.Reactions.COMMENT_DELAY_MILLIS - Constants.Reactions.FIRST_LIKE_DELAY_MILLIS)
            AppRepository.addComment(postId, COMMENTER, "Welcome $firstName! 👋")

            delay(Constants.Reactions.SECOND_LIKES_DELAY_MILLIS - Constants.Reactions.COMMENT_DELAY_MILLIS)
            AppRepository.addReaction(postId, SECOND_LIKER, ReactionType.LIKE)
            AppRepository.addReaction(postId, THIRD_LIKER, ReactionType.LIKE)
        }
    }

    // Called by AppRepository once a post's combined reaction+comment count
    // reaches three, wherever that lands in the sequence above.
    fun sendThirdReactionNotification() {
        val context = appContext ?: return
        val granted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
        if (!granted) return

        val openIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_OPEN_NOTIFICATIONS, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(context, R.color.vmp_blue))
            .setContentTitle("VMP Messenger")
            .setContentText("3 people reacted to your first post 🎉")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Post reactions",
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        appContext?.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }
}
