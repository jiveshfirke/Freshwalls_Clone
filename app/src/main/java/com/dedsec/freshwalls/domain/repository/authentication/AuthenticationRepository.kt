package com.dedsec.freshwalls.domain.repository.authentication

import com.dedsec.freshwalls.data.remote.dto.authentication.LoginWithProviderDto

interface AuthenticationRepository {
    suspend fun loginWithProvider(name: String, email: String): LoginWithProviderDto
}