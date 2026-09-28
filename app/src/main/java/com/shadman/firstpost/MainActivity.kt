package com.shadman.firstpost

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.navigation.AppNavHost
import com.shadman.firstpost.notifications.ReactionScheduler
import com.shadman.firstpost.ui.theme.FirstPostTheme

class MainActivity : ComponentActivity() {

    // The reaction system notification's tap target carries this extra so
    // the app knows to jump straight to Notifications when it's tapped.
    companion object {
        const val EXTRA_OPEN_NOTIFICATIONS = "open_notifications"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Draw behind the status and navigation bars. The app is always dark,
        // so the bar icons are always light, whatever the phone's theme.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        // Needs a real Context to create the notification channel and post
        // to it later; ReactionScheduler itself is a plain Kotlin object
        // with no Android dependency until this is called.
        ReactionScheduler.init(applicationContext)
        handleNotificationIntent(intent)
        setContent {
            FirstPostTheme {
                // Every screen renders inside this one Surface. Surface sets
                // LocalContentColor to the theme's onBackground, so every
                // Text everywhere defaults to the light colour automatically
                // — no screen has to set a text colour just to be visible.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavHost()
                }
            }
        }
    }

    // launchMode="singleTop" routes a notification tap here instead of
    // creating a second Activity instance, so the already-running app (and
    // AppRepository's in-memory state) stays exactly as it was.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.getBooleanExtra(EXTRA_OPEN_NOTIFICATIONS, false) == true) {
            AppRepository.requestOpenNotifications()
        }
    }
}
