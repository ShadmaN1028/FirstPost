package com.shadman.firstpost.onboarding

import androidx.lifecycle.ViewModel
import com.shadman.firstpost.Constants
import com.shadman.firstpost.data.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class WelcomeUiState(
    val isAccountPickerVisible: Boolean = false,
    val isSignedIn: Boolean = false,
)

// Owns the welcome screen's state: whether the mock account picker sheet is
// open, and whether sign-in has finished. WelcomeScreen and the picker sheet
// stay dumb — they just render this and call back.
class WelcomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun onSignInWithGoogleClick() {
        _uiState.value = _uiState.value.copy(isAccountPickerVisible = true)
    }

    fun onDismissAccountPicker() {
        _uiState.value = _uiState.value.copy(isAccountPickerVisible = false)
    }

    // The picker only ever offers one invented account, so there's nothing
    // to pass in — it's always Constants.MockGoogleAccount.
    fun onAccountSelected() {
        AppRepository.signIn(
            name = Constants.MockGoogleAccount.NAME,
            email = Constants.MockGoogleAccount.EMAIL,
        )
        _uiState.value = _uiState.value.copy(isAccountPickerVisible = false, isSignedIn = true)
    }
}
