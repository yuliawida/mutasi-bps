package com.polstat.mutasibps.model

data class MutationRequest(
    val id: Long?,
    val provinsiTujuan: String,
    val kabupatenTujuan: String,
    val jabatanTujuan: String,
    val unitKerjaTujuan: String,
    val status: String, // "approved" or "rejected"
    val user: User? // Relasi ke User
)
