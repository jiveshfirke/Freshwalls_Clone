package com.dedsec.freshwalls.presentation.authentication.state

import com.dedsec.freshwalls.domain.model.authentication.UserData

data class SignInWithGoogleState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val userData: UserData? = null
)
