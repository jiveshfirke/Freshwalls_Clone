package com.dedsec.freshwalls.presentation.authentication.viewmodel

import androidx.lifecycle.ViewModel
import com.dedsec.freshwalls.domain.model.authentication.SignInResult
import com.dedsec.freshwalls.presentation.authentication.state.SignInWithGoogleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInWithGoogleViewModel: ViewModel() {
    private val _signInWithGoogleState = MutableStateFlow(SignInWithGoogleState())
    val signInState = _signInWithGoogleState.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _signInWithGoogleState.update {
            it.copy(
                userData = result.userData,
                isSignInSuccessful = result.userData != null,
                signInError = result.errorMessage
            )
        }
    }
    fun resetSignInState() {
        _signInWithGoogleState.update { SignInWithGoogleState() }
    }
}