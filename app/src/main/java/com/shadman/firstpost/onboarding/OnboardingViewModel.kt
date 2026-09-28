package com.shadman.firstpost.onboarding

import androidx.lifecycle.ViewModel
import com.shadman.firstpost.data.AppRepository
import com.shadman.firstpost.data.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Everything the user enters across onboarding steps 01-08, in one place,
// so step 08 (Say hello) can read the photo and name that steps 01-02 set.
data class OnboardingDraft(
    val photoUri: String? = null,
    val name: String = "",
    val username: String = "",
    val bio: String = "",
    val gender: String? = null,
    val dateOfBirthMillis: Long? = null,
    val interestedIn: String? = null,
    val caption: String = "",
) {
    // Steps 02, 03 and 06 require a value before Next enables; 01, 04, 05
    // and 07 are skippable, so they have no "invalid" state.
    val isNameValid: Boolean get() = name.isNotBlank()
    val isUsernameValid: Boolean get() = isValidUsername(username)
    val isDateOfBirthValid: Boolean get() = dateOfBirthMillis != null
}

// Scoped to the "onboarding" nav graph (see AppNavHost), not to one screen,
// so every step 01-08 composable shares this same instance instead of each
// getting its own empty one.
class OnboardingViewModel : ViewModel() {

    private val _draft = MutableStateFlow(OnboardingDraft())
    val draft: StateFlow<OnboardingDraft> = _draft.asStateFlow()

    // Called once, right after mock sign-in, so step 01 already shows a name.
    fun prefillName(name: String) {
        _draft.value = _draft.value.copy(name = name)
    }

    fun setPhoto(uri: String?) {
        _draft.value = _draft.value.copy(photoUri = uri)
    }

    fun setName(name: String) {
        _draft.value = _draft.value.copy(name = name)
    }

    fun setUsername(username: String) {
        _draft.value = _draft.value.copy(username = username)
    }

    fun setBio(bio: String) {
        _draft.value = _draft.value.copy(bio = bio)
    }

    fun setGender(gender: String?) {
        _draft.value = _draft.value.copy(gender = gender)
    }

    fun setDateOfBirth(millis: Long?) {
        _draft.value = _draft.value.copy(dateOfBirthMillis = millis)
    }

    fun setInterestedIn(interestedIn: String?) {
        _draft.value = _draft.value.copy(interestedIn = interestedIn)
    }

    // Step 03 opens with a username guessed from the name, but only if the
    // user hasn't already typed one (e.g. coming back via Back).
    fun suggestUsernameIfBlank() {
        if (_draft.value.username.isBlank()) {
            _draft.value = _draft.value.copy(username = suggestUsername(_draft.value.name))
        }
    }

    fun setCaption(caption: String) {
        _draft.value = _draft.value.copy(caption = caption)
    }

    // Step 08 opens with the first suggested caption, but only the first
    // time — editing it and coming back via Back shouldn't overwrite it.
    fun prefillCaptionIfBlank(firstName: String) {
        if (_draft.value.caption.isBlank()) {
            _draft.value = _draft.value.copy(caption = captionSuggestions(firstName).first())
        }
    }

    // Turns the current draft into the user's first real post.
    fun postFirstHello(): Post =
        AppRepository.addPost(text = _draft.value.caption, photoUri = _draft.value.photoUri)

    // Copies username/bio/gender/birthday/interested-in into the real user
    // record. Called once on reaching Say hello — steps 03-07 are already
    // decided by then, whether the user goes on to Post or Skip.
    fun finalizeProfile() {
        AppRepository.updateProfile(
            username = _draft.value.username,
            bio = _draft.value.bio,
            gender = _draft.value.gender,
            dateOfBirthMillis = _draft.value.dateOfBirthMillis,
            interestedIn = _draft.value.interestedIn,
        )
    }
}
