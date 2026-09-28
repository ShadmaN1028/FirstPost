package com.shadman.firstpost.data

import com.shadman.firstpost.Constants
import com.shadman.firstpost.notifications.ReactionScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// The single in-memory source of truth for the whole app. Every screen reads
// from these flows, so a change made on one screen shows up everywhere.
// Nothing is saved to disk: restarting the app starts over, on purpose.
object AppRepository {

    // Everyone in the app except the current user.
    val users: List<User> = SyntheticData.users
    val circleCandidates: List<User> = SyntheticData.circleCandidates

    // null until onboarding creates the user.
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // The invented new members' posts are seeded in here too (not kept
    // separate), so liking one behaves exactly like liking any other post —
    // isFirstPost is what the feed uses to pull them into "New members".
    private val _posts = MutableStateFlow(SyntheticData.posts + SyntheticData.newMemberPosts)
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    val stories: List<Story> = SyntheticData.stories

    // A new user starts with no friends. "Build your circle" fills this in.
    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // True right after a post earns the trial; the screen that made the
    // post reads and clears this immediately to show the earned-Premium
    // dialog itself — see Step08SayHelloScreen / ComposerScreen.
    private val _celebrationPending = MutableStateFlow(false)
    val celebrationPending: StateFlow<Boolean> = _celebrationPending.asStateFlow()

    // Set when the reaction system notification is tapped; MainScaffold
    // watches this and jumps to Notifications, then clears it.
    private val _openNotificationsRequested = MutableStateFlow(false)
    val openNotificationsRequested: StateFlow<Boolean> = _openNotificationsRequested.asStateFlow()

    // Posts store only an authorId, so screens look the author up here.
    fun userById(id: String): User? =
        if (id == _currentUser.value?.id) _currentUser.value else users.find { it.id == id }

    // Called once, after the mock Google account picker. Creates the current
    // user with just an identity — the rest of the profile fills in as
    // onboarding steps 01-08 run. signedUpAtMillis starts the 24h window a
    // first post has to land in to earn the trial.
    fun signIn(name: String, email: String) {
        _currentUser.value = User(
            id = CURRENT_USER_ID,
            name = name,
            username = "",
            avatarColor = Constants.MockGoogleAccount.AVATAR_COLOR,
            email = email,
            signedUpAtMillis = System.currentTimeMillis(),
        )
    }

    private const val CURRENT_USER_ID = "me"

    // The one way a post is ever created for the current user, whether from
    // Say hello or the feed composer. A photo only ever comes from Say
    // hello, and only ever means "updated their profile picture" — so it
    // also becomes the user's own profile photo.
    fun addPost(text: String, photoUri: String? = null): Post {
        val user = checkNotNull(_currentUser.value) { "addPost called before sign-in" }
        val isFirstPost = _posts.value.none { it.authorId == user.id }
        val post = Post(
            id = "post_${System.currentTimeMillis()}",
            authorId = user.id,
            text = text,
            imageUri = photoUri,
            createdAtMillis = System.currentTimeMillis(),
            isProfilePictureUpdate = photoUri != null,
            isFirstPost = isFirstPost,
        )
        _posts.value = listOf(post) + _posts.value
        if (photoUri != null) {
            _currentUser.value = _currentUser.value?.copy(photoUri = photoUri)
        }
        if (isFirstPost) {
            startTrialIfEligible(user)
            ReactionScheduler.scheduleFor(post.id, firstNameOf(user.name))
        }
        return post
    }

    // Copies the rest of onboarding's draft (steps 03-07) into the real user
    // record. Called once, when Say hello is reached — by then every field
    // is already decided, whether the user goes on to Post or Skip.
    fun updateProfile(username: String, bio: String, gender: String?, dateOfBirthMillis: Long?, interestedIn: String?) {
        _currentUser.value = _currentUser.value?.copy(
            username = username,
            bio = bio.ifBlank { null },
            gender = gender,
            dateOfBirthMillis = dateOfBirthMillis,
            interests = listOfNotNull(interestedIn),
        )
    }

    fun toggleLike(postId: String) {
        val userId = _currentUser.value?.id ?: return
        _posts.value = _posts.value.map { post ->
            if (post.id != postId) return@map post
            val alreadyLiked = post.reactions.any { it.userId == userId }
            val reactions = if (alreadyLiked) {
                post.reactions.filterNot { it.userId == userId }
            } else {
                post.reactions + Reaction(userId, ReactionType.LIKE)
            }
            post.copy(reactions = reactions)
        }
    }

    fun isLikedByCurrentUser(post: Post): Boolean {
        val userId = _currentUser.value?.id ?: return false
        return post.reactions.any { it.userId == userId }
    }

    // Called only by ReactionScheduler — a synthetic reactor liking the
    // user's first post. Distinct from toggleLike, which is the current
    // user liking someone else's post: this never removes a reaction.
    fun addReaction(postId: String, userId: String, type: ReactionType) {
        if (_posts.value.find { it.id == postId }?.reactions?.any { it.userId == userId } == true) return
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(reactions = post.reactions + Reaction(userId, type)) else post
        }
        val actorName = userById(userId)?.name ?: "Someone"
        addNotification(
            type = NotificationType.REACTION,
            text = "$actorName liked your post",
            postId = postId,
        )
        notifySystemIfThresholdReached(postId)
    }

    fun addComment(postId: String, userId: String, text: String) {
        val comment = Comment(
            id = "comment_${System.currentTimeMillis()}",
            authorId = userId,
            text = text,
            createdAtMillis = System.currentTimeMillis(),
        )
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) post.copy(comments = post.comments + comment) else post
        }
        val actorName = userById(userId)?.name ?: "Someone"
        addNotification(
            type = NotificationType.COMMENT,
            text = "$actorName commented: $text",
            postId = postId,
        )
        notifySystemIfThresholdReached(postId)
    }

    private fun addNotification(type: NotificationType, text: String, postId: String?) {
        val notification = AppNotification(
            id = "notif_${System.currentTimeMillis()}_${_notifications.value.size}",
            type = type,
            text = text,
            createdAtMillis = System.currentTimeMillis(),
            postId = postId,
        )
        _notifications.value = listOf(notification) + _notifications.value
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun requestOpenNotifications() {
        _openNotificationsRequested.value = true
    }

    fun consumeOpenNotificationsRequest() {
        _openNotificationsRequested.value = false
    }

    // Fires the one real system notification, exactly when the running
    // total of reactions+comments on that post first reaches the threshold
    // — not on every reaction after that.
    private fun notifySystemIfThresholdReached(postId: String) {
        val post = _posts.value.find { it.id == postId } ?: return
        val total = post.reactions.size + post.comments.size
        if (total == Constants.Reactions.SYSTEM_NOTIFICATION_THRESHOLD) {
            ReactionScheduler.sendThirdReactionNotification()
        }
    }

    fun setFriends(selected: List<User>) {
        _friends.value = selected
    }

    fun addFriend(user: User) {
        if (_friends.value.none { it.id == user.id }) {
            _friends.value = _friends.value + user
        }
    }

    // The first post made within 24h of signing up earns 2 days of Premium.
    // Nobody gets a trial otherwise — see CLAUDE.md's earned-trial design.
    private fun startTrialIfEligible(userAtPostTime: User) {
        val signedUpAt = userAtPostTime.signedUpAtMillis ?: return
        val withinWindow = System.currentTimeMillis() - signedUpAt <= Constants.Premium.FIRST_POST_WINDOW_MILLIS
        if (!withinWindow) return
        val trialEnd = System.currentTimeMillis() + Constants.Premium.TRIAL_DURATION_MILLIS
        _currentUser.value = _currentUser.value?.copy(trialEndMillis = trialEnd)
        _celebrationPending.value = true
    }

    fun consumeCelebration() {
        _celebrationPending.value = false
    }

    // Demo-only control (Settings screen): ends the trial immediately
    // instead of waiting two real days for it to lapse naturally.
    fun endTrialNow() {
        _currentUser.value = _currentUser.value?.copy(trialEndMillis = System.currentTimeMillis() - 1)
    }

    fun premiumStatus(user: User?): PremiumStatus {
        val trialEnd = user?.trialEndMillis ?: return PremiumStatus.None
        val millisLeft = trialEnd - System.currentTimeMillis()
        if (millisLeft <= 0) return PremiumStatus.Expired
        val oneDayMillis = 24 * 60 * 60 * 1000L
        // Round up: 1ms left still reads as "1 day left", not "0 days left".
        val daysLeft = ((millisLeft + oneDayMillis - 1) / oneDayMillis).toInt().coerceAtLeast(1)
        return PremiumStatus.Trial(daysLeft = daysLeft)
    }

    // Kept local (not shared with onboarding.firstNameOf) so this data-layer
    // file has no dependency on a feature package.
    private fun firstNameOf(name: String): String = name.trim().substringBefore(' ').ifBlank { "there" }
}
