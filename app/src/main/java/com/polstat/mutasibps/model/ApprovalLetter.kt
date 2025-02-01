package com.polstat.mutasibps.model

data class ApprovalLetter(
    val id: Long?,
    val approvalNumber: String,
    val letterContent: String,
    val mutationRequest: MutationRequest?, // Relasi ke MutationRequest
    val user: User? // Relasi ke User
)
