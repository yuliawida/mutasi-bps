package com.polstat.mutasibps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.mutasibps.model.LoginRequest
import com.polstat.mutasibps.model.LoginResponse
import com.polstat.mutasibps.model.UserRequest
import com.polstat.mutasibps.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Response<LoginResponse>>()
    val loginResult: LiveData<Response<LoginResponse>> = _loginResult

    private val _registerResult = MutableLiveData<Response<Unit>>()
    val registerResult: LiveData<Response<Unit>> = _registerResult

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginResult.value = repository.login(loginRequest)
        }
    }

    fun register(userRequest: UserRequest) {
        viewModelScope.launch {
            _registerResult.value = repository.register(userRequest)
        }
    }

}
