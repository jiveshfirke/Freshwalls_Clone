package com.dedsec.freshwalls.domain.use_case.authentication

import com.dedsec.freshwalls.common.Resource
import com.dedsec.freshwalls.data.remote.dto.authentication.toUser
import com.dedsec.freshwalls.domain.model.authentication.User
import com.dedsec.freshwalls.domain.repository.authentication.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(name: String, email: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val user = authenticationRepository.loginWithProvider(name = name, email = email).toUser()
            emit(Resource.Success(data = user))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, please check your Internet connection"))
        }
    }
}