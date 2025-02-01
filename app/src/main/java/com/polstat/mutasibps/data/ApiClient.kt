package com.polstat.mutasibps.data

import android.content.Context
import com.polstat.mutasibps.service.ApiService
import com.polstat.mutasibps.utils.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.1.9:8080/"

    fun getApiService(context: Context): ApiService {
        val tokenManager = SharedPreferencesManager(context)

        // Interceptor untuk menyisipkan token secara otomatis ke dalam header
        val authInterceptor = Interceptor { chain ->
            val token = tokenManager.getToken()
            val request: Request = chain.request().newBuilder()
                .apply {
                    if (!token.isNullOrEmpty()) {
                        addHeader("Authorization", "Bearer $token")
                    }
                }
                .build()
            chain.proceed(request)
        }

        // Logging untuk melihat request dan response API
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Tambahkan interceptor untuk token
            .addInterceptor(loggingInterceptor) // Tambahkan logging
            .build()

        // Retrofit builder dengan client yang sudah di-set
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
