package com.dedsec.freshwalls.presentation.authentication.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedsec.freshwalls.common.Resource
import com.dedsec.freshwalls.domain.use_case.authentication.AuthenticationUseCase
import com.dedsec.freshwalls.presentation.authentication.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {
    private val _authenticationState = mutableStateOf(AuthenticationState())
    val authenticationState: State<AuthenticationState> = _authenticationState

    fun loginWithProvider(name: String, email: String) {
        println("Printing name $name and email $email")
        authenticationUseCase(name = name, email = email).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _authenticationState.value = AuthenticationState(
                        user = result.data
                    )
                }
                is Resource.Error -> {
                    _authenticationState.value = AuthenticationState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _authenticationState.value = AuthenticationState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}