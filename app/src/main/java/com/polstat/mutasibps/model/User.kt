package com.polstat.mutasibps.model

data class User(
    val username: String,
    val password: String,
    val name: String,
    val nip: String,
    val jabatan: String,
    val unitKerja: String,
    val role: String
)
