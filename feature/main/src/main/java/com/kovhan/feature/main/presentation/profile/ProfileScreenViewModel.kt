package com.kovhan.feature.main.presentation.profile

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.GetUserUseCase
import com.kovhan.domain.auth.SignOutUseCase
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenEffect
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenIntent
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    getUser: GetUserUseCase,
    private val signOut: SignOutUseCase,
) : BaseViewModel<ProfileScreenState, ProfileScreenEffect>(ProfileScreenState()),
    ProfileScreenIntent {

    init {
        getUser()
            .onEach { user -> publishState { copy(user = user) } }
            .launchIn(viewModelScope)
    }

    override fun onSignOutClicked() {
        if (uiState.value.isSigningOut) return
        publishState { copy(isSigningOut = true) }
        viewModelScope.launch {
            signOut()
            publishState { copy(isSigningOut = false) }
            publishEffect(ProfileScreenEffect.NavigateToAuth)
        }
    }
}
