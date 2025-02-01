package com.polstat.mutasibps.model

data class MutationRequestRequest(
    val provinsiTujuan: String,
    val kabupatenTujuan: String,
    val jabatanTujuan: String,
    val unitKerjaTujuan: String,
    val status: String
)
