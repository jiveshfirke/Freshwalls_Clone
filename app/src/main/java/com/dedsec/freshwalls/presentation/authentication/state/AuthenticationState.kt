package com.dedsec.freshwalls.presentation.authentication.state

import com.dedsec.freshwalls.domain.model.authentication.User

data class AuthenticationState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String = ""
)
