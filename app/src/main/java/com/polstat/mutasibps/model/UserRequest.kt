package com.polstat.mutasibps.model

data class UserRequest(
    val username: String,
    val password: String,
    val name: String,
    val nip: String,
    val unitKerja: String,
    val jabatan: String,
    val role: Role // Ubah tipe dari String ke Role
)

