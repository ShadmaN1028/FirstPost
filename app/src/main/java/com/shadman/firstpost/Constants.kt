package com.shadman.firstpost

// Every tunable number and text template lives here, never scattered in screens.
object Constants {

    // Onboarding: the "01 / 08" counter. Say hello is step 8.
    const val ONBOARDING_STEP_COUNT = 8

    // The single invented account shown in the mock Google account picker.
    object MockGoogleAccount {
        const val NAME = "Alex Rivera"
        const val EMAIL = "alex.rivera.demo@gmail.com"
        const val AVATAR_COLOR = 0xFF0071EBL
    }

    // Step 08, Say hello, and the feed composer's suggestion chips. "%s" is
    // replaced with the user's first name; a template with no "%s" (like the
    // third one) just ignores the argument.
    object SayHello {
        val CAPTION_TEMPLATES = listOf(
            "Hello people! I'm %s, new here 👋",
            "Hi everyone, %s here! Say hi 👋",
            "Just joined VMP. Excited to meet you all!",
        )
    }

    // The earned-trial offer: post your first post in time, get Premium free.
    object Premium {
        const val TRIAL_DURATION_MILLIS = 2 * 24 * 60 * 60 * 1000L
        const val FIRST_POST_WINDOW_MILLIS = 24 * 60 * 60 * 1000L
    }

    // The synthetic "boost" a first post gets, run by ReactionScheduler.
    // Delays are measured from the moment the post is created.
    object Reactions {
        const val FIRST_LIKE_DELAY_MILLIS = 8_000L
        const val COMMENT_DELAY_MILLIS = 15_000L
        const val SECOND_LIKES_DELAY_MILLIS = 25_000L
        // The count of reactions+comments that triggers the system notification.
        const val SYSTEM_NOTIFICATION_THRESHOLD = 3
    }
}
