package com.dedsec.freshwalls.data.repository.authentication

import com.dedsec.freshwalls.data.remote.FWApi
import com.dedsec.freshwalls.data.remote.dto.authentication.LoginWithProviderDto
import com.dedsec.freshwalls.domain.repository.authentication.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val fwApi: FWApi
): AuthenticationRepository {
    override suspend fun loginWithProvider(name: String, email: String): LoginWithProviderDto {
        return fwApi.loginWithProvider(name = name, email = email)
    }
}