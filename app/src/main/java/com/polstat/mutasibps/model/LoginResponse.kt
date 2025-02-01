package com.polstat.mutasibps.model

data class LoginResponse(
    val token: String,
    val role: String, // Menambahkan role ke dalam response,
    val user: User
)
