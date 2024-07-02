package com.dedsec.freshwalls.data.remote.dto.authentication

import com.dedsec.freshwalls.domain.model.authentication.User
import com.dedsec.freshwalls.data.remote.dto.authentication.User as User1

data class LoginWithProviderDto(
    val token: String,
    val user: User1
)

fun LoginWithProviderDto.toUser(): User {
    return User(
        identifier = user.original.data.identifier,
        name = user.original.data.name,
        email = user.original.data.email,
        token = token,
    )
}