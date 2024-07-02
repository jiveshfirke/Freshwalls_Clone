package com.dedsec.freshwalls.domain.model.authentication

data class SignInResult(
    val userData: UserData?,
    val errorMessage: String?
)

data class UserData(
    val email: String,
    val username: String?,
    val profilePictureUrl: String?
)
