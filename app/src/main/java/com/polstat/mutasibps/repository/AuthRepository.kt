package com.polstat.mutasibps.repository

import android.content.Context
import com.polstat.mutasibps.data.ApiClient
import com.polstat.mutasibps.model.LoginRequest
import com.polstat.mutasibps.model.LoginResponse
import com.polstat.mutasibps.model.UserRequest
import retrofit2.Response

class AuthRepository(context: Context) {

    private val apiService = ApiClient.getApiService(context)

    // Fungsi untuk login
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }

    // Fungsi register
    suspend fun register(userRequest: UserRequest): Response<Unit> {
        return apiService.register(userRequest)
    }
}
