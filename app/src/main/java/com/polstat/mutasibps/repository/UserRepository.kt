package com.polstat.mutasibps.repository

import android.content.Context
import com.polstat.mutasibps.data.ApiClient
import com.polstat.mutasibps.model.User
import com.polstat.mutasibps.model.UserRequest
import com.polstat.mutasibps.service.ApiService
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository(context: Context) {

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://your.api.url/") // Ganti dengan URL API Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    // Fungsi untuk mendapatkan data profil user
    suspend fun getProfile(token: String): Response<User> {
        return apiService.getProfile("Bearer $token")
    }

    // Fungsi untuk memperbarui data profil user
    suspend fun updateProfile(token: String, userRequest: UserRequest): Response<User> {
        return apiService.updateProfile("Bearer $token", userRequest)
    }

    suspend fun changePassword(token: String, newPassword: String): Response<Unit> {
        return apiService.changePassword("Bearer $token", newPassword)
    }

    suspend fun deleteAccount(token: String): Response<Unit> {
        return apiService.deleteAccount("Bearer $token")
    }
}
